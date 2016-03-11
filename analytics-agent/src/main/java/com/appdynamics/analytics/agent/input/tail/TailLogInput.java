/*     */package com.appdynamics.analytics.agent.input.tail;

/*     */
/*     *//*     */import java.io.IOException;
/*     */
import java.io.UnsupportedEncodingException;
/*     */
import java.nio.ByteBuffer;
/*     */
import java.nio.channels.FileChannel;
/*     */
import java.nio.charset.Charset;
/*     */
import java.util.concurrent.ExecutorService;
/*     */
import java.util.concurrent.Future;
/*     */
import java.util.concurrent.atomic.AtomicBoolean;

/*     */
import org.apache.commons.io.IOUtils;
/*     */
import org.slf4j.Logger;
/*     */
import org.slf4j.LoggerFactory;

import com.appdynamics.analytics.agent.input.LogInput;
/*     */
import com.appdynamics.analytics.agent.source.LogComponentFactory;
/*     */
import com.appdynamics.analytics.pipeline.framework.Pipeline;
/*     */
import com.appdynamics.analytics.pipeline.framework.PipelinesHealthCheck;
/*     */
import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */
import com.appdynamics.common.util.priority.PriorityRunnable;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class TailLogInput
/*     */extends LogInput
/*     */{
    /* 35 */private static final Logger log                 = LoggerFactory.getLogger(TailLogInput.class);
    /*     */
    /* 37 */private static final String DEFAULT_CHARSET     = Charset.defaultCharset().name();
    /*     */public static final int        READ_BUFFSIZE_BYTES = 65536;
    /*     */private static final long      NUM_TIMES_WAIT      = 10L;
    /*     */private final AtomicBoolean    tailing;
    
    /*     */
    /* 42 */public LogTailer getLogTailer() {
        return this.logTailer;
    }
    
    /*     */
    /*     */private final TailLogInputConfiguration inputConfiguration;
    /*     */private final LogTailer                 logTailer;
    /*     */private volatile Future                 future;
    
    /*     */public TailLogInput(TailFileState tailState, TailLogInputConfiguration configuration, LogComponentFactory factory, ExecutorService executorService)
    /*     */{
        /* 49 */super("Tail Log Input:" + tailState.getFilename(), factory, executorService);
        /* 50 */log.info("Initiating Tail Log Input [{}]", tailState.getFilename());
        /* 51 */this.tailing = new AtomicBoolean(false);
        /* 52 */this.inputConfiguration = configuration;
        /* 53 */this.logTailer = new LogTailer(tailState, configuration, factory, null);
        /*     */}
    
    /*     */
    /*     */public boolean isTailing() {
        /* 57 */return this.tailing.get();
        /*     */}
    
    /*     */
    /*     */public TailFileState getTailState() {
        /* 61 */return getLogTailer().tailState;
        /*     */}
    
    /*     */
    /*     */@Override
    public HealthCheck.Result check()
    /*     */{
        /* 66 */StringBuilder sb = new StringBuilder();
        /* 67 */sb.append(getTailState().getFilename()).append(' ');
        /* 68 */PipelinesHealthCheck.printStatus(this.logTailer.pipeline, sb);
        /* 69 */Future task = this.future;
        /* 70 */boolean pipelineHealthy = !PipelinesHealthCheck.isUnhealthy(this.logTailer.pipeline.getState());
        /* 71 */boolean overallHealthy = ((isRunning()) && (task != null) && (!task.isDone()) && (!task.isCancelled())) || ((!isRunning()) && (pipelineHealthy));
        /*     */
        /*     */
        /* 74 */return overallHealthy ? Result.healthy(sb.toString()) : Result.unhealthy(sb.toString());
        /*     */}
    
    /*     */
    /*     */@Override
    public synchronized void start()
    /*     */{
        /* 79 */if (!isRunning()) {
            /* 80 */super.start();
            /* 81 */this.future = this.executorService.submit(this.logTailer);
            /*     */}
        /*     */}
    
    /*     */
    /*     */@Override
    public synchronized void stop()
    /*     */{
        /* 87 */if (isRunning()) {
            /* 88 */super.stop();
            /* 89 */if (this.future != null)
                /* 90 */ConcurrencyHelper.cancel(this.future, 10L * this.inputConfiguration.getTailInterval().getTime(), true, log);
            /*     */}
        /*     */}
    
    /*     */
    /*     */class LogTailer extends PriorityRunnable {
        private static final char NEW_LINE_CHAR = '\n';
        
        /*     */
        /* 96 */@Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof LogTailer))
                return false;
            LogTailer other = (LogTailer) o;
            if (!other.canEqual(this))
                return false;
            if (this.start != other.start)
                return false;
            if (this.startReadPosition != other.startReadPosition)
                return false;
            if (this.bufferReadPosition != other.bufferReadPosition)
                return false;
            return this.capRemainderOfLine == other.capRemainderOfLine;
        }
        
        public boolean canEqual(Object other) {
            return other instanceof LogTailer;
        }
        
        @Override
        public int hashCode() {
            int PRIME = 31;
            int result = 1;
            result = result * 31 + this.start;
            result = result * 31 + this.startReadPosition;
            result = result * 31 + this.bufferReadPosition;
            result = result * 31 + (this.capRemainderOfLine ? 1231 : 1237);
            return result;
            /*     */}
        
        /*     */
        /* 99 */private final byte[]      readBuffer         = new byte[65536];
        /*     */
        /*     */private final FileChannel    randomAccessFile;
        /*     */
        /*     */final Pipeline<CharSequence> pipeline;
        /*     */
        /*     */volatile TailFileState       tailState;
        /* 106 */private int              start              = 0;
        /*     */
        /*     */
        /*     */
        /*     */
        /* 111 */private int              startReadPosition  = 0;
        /*     */
        /*     */
        /*     */
        /*     */
        /* 116 */private int              bufferReadPosition = 0;
        /*     */
        /*     */
        /*     */
        /*     */
        /* 121 */private boolean          capRemainderOfLine = false;
        
        /*     */
        /*     */
        /*     */
        /*     */private LogTailer(TailFileState tailFileState, TailLogInputConfiguration configuration, LogComponentFactory factory)
        /*     */{
            /* 127 */super();
            /* 128 */this.tailState = tailFileState;
            /*     */
            /*     */
            /* 131 */if (this.tailState.getLastReadTimestamp() == 0L) {
                /* 132 */this.tailState.setLastReadTimestamp(System.currentTimeMillis());
                /*     */}
            /* 134 */this.randomAccessFile = this.tailState.takeRandomAccessFileOwnership();
            /* 135 */this.pipeline = factory.createPipeline(configuration.getPipelineConfiguration());
            /*     */}
        
        /*     */
        /*     */@Override
        public void run()
        /*     */{
            /*     */try {
                /* 141 */TailLogInput.log.info("Starting to tail file [{}]", this.tailState.getFilename());
                /* 142 */if (this.tailState.getLastReadPosition() > 0L) {
                    /* 143 */TailLogInput.log.info("Resuming tailer from last read byte [{}] position", Long.valueOf(this.tailState.getLastReadPosition()));
                    /*     */}
                /* 145 */this.pipeline.start();
                /* 146 */TailLogInput.this.tailing.set(true);
                /* 147 */readLogFile();
                /*     */} catch (Throwable e) {
                /* 149 */TailLogInput.log.error(String.format("Aborting tailing of file [%s] with signature [%s]", new Object[] { this.tailState.getFilename(), this.tailState.getSignature() }), e);
                /*     */}
            /*     */finally
            /*     */{
                /* 153 */IOUtils.closeQuietly(this.randomAccessFile);
                /* 154 */flushAndShutdownPipeline();
                /* 155 */TailLogInput.this.tailing.set(false);
                /* 156 */TailLogInput.log.info("Stopped tailing file [{}]", this.tailState.getFilename());
                /*     */}
            /*     */}
        
        /*     */
        /*     */private void flushAndShutdownPipeline() {
            /* 161 */TailLogInput.log.debug("About to flush contents");
            /*     */try
            /*     */{
                /* 164 */this.pipeline.managedCall(null);
                /* 165 */if (Thread.currentThread().isInterrupted())
                /*     */{
                    /* 167 */TailLogInput.log.warn("The contents may not have been flushed completely");
                    /*     */} else {
                    /* 169 */TailLogInput.log.debug("The contents were flushed successfully");
                    /*     */}
                /*     */} catch (Exception e) {
                /* 172 */TailLogInput.log.warn("The contents could not be flushed completely, so there may be some data loss for the input [" + this.tailState.getFilename() + "] pipeline", e);
                /*     */}
            /*     */finally {
                /* 175 */this.pipeline.stop();
                /*     */}
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */private long getSeekPosition()
        /*     */throws IOException
        /*     */{
            /* 220 */if (TailLogInput.this.inputConfiguration.isStartAtEnd()) {
                /* 221 */long logFileLength = this.randomAccessFile.size();
                /* 222 */if (logFileLength == 0L) {
                    /* 223 */return 0L;
                    /*     */}
                /* 225 */long readOffset = logFileLength < 65536L ? 0L : logFileLength - 65536L;
                /* 226 */this.randomAccessFile.position(readOffset);
                /* 227 */int numBytes = this.randomAccessFile.read(ByteBuffer.wrap(this.readBuffer));
                /* 228 */int count = 0;
                /* 229 */while ((numBytes-- > 0) &&
                /* 230 */(this.readBuffer[numBytes] != 10))
                /*     */{
                    /*     */
                    /* 233 */count++;
                    /*     */}
                /* 235 */if (numBytes > 0) {
                    /* 236 */return logFileLength - count;
                    /*     */}
                /* 238 */return logFileLength;
                /*     */}
            /*     */
            /* 241 */return this.tailState.getLastReadPosition();
            /*     */}
        
        /*     */
        /*     */private void tryProcessBufferIntoLines() throws UnsupportedEncodingException
        /*     */{
            /* 246 */int current = this.startReadPosition;
            /* 247 */int end = this.bufferReadPosition;
            /* 248 */while (current < end) {
                /* 249 */if (this.readBuffer[current] == 10) {
                    /* 250 */int lineLength = current - this.start;
                    /* 251 */if (this.capRemainderOfLine)
                    /*     */{
                        /* 253 */this.capRemainderOfLine = false;
                        /*     */}
                    /*     */else {
                        /* 256 */processLogLine(this.start, lineLength);
                        /*     */}
                    /* 258 */increaseLastReadPosition(lineLength + 1);
                    /* 259 */this.start = (current + 1);
                    /*     */}
                /* 261 */current++;
                /*     */}
            /* 263 */this.startReadPosition = current;
            /* 264 */if (end == this.readBuffer.length)
            /*     */{
                /* 266 */if (this.start == 0)
                /*     */{
                    /* 268 */if (!this.capRemainderOfLine)
                    /*     */{
                        /* 270 */processLogLine(0, end);
                        /*     */
                        /* 272 */this.capRemainderOfLine = true;
                        /*     */}
                    /* 274 */increaseLastReadPosition(end);
                    /* 275 */this.bufferReadPosition = 0;
                    /* 276 */} else if (this.start == end)
                /*     */{
                    /*     */
                    /* 279 */this.start = 0;
                    /*     */
                    /* 281 */this.bufferReadPosition = 0;
                    /*     */}
                /*     */else {
                    /* 284 */System.arraycopy(this.readBuffer, this.start, this.readBuffer, 0, end - this.start);
                    /* 285 */this.bufferReadPosition = (end - this.start);
                    /* 286 */this.start = 0;
                    /*     */}
                /* 288 */this.startReadPosition = 0;
                /*     */}
            /*     */}
        
        /*     */
        /*     */private void processLogLine(int offset, int length) throws UnsupportedEncodingException {
            /* 293 */if (length > 0) {
                /* 294 */String logLine = new String(this.readBuffer, offset, length, TailLogInput.DEFAULT_CHARSET);
                /* 295 */this.pipeline.managedCall(logLine);
                /*     */}
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */private void increaseLastReadPosition(int incr)
        /*     */{
            /* 308 */this.tailState.setLastReadPosition(this.tailState.getLastReadPosition() + incr);
            /*     */}
        
        /*     */
        /*     *//* Error */
        /*     */private void readLogFile()
        /*     */throws IOException
        /*     */{
            /*     */// Byte code:
            /*     */// 0: aload_0
            /*     */// 1: getfield 18
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:randomAccessFile
            // Ljava/nio/channels/FileChannel;
            /*     */// 4: ifnonnull +23 -> 27
            /*     */// 7: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 10: ldc 59
            /*     */// 12: aload_0
            /*     */// 13: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 16: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 19: invokeinterface 60 3 0
            /*     */// 24: goto +25 -> 49
            /*     */// 27: aload_0
            /*     */// 28: invokespecial 61
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:getSeekPosition
            // ()J
            /*     */// 31: lstore_1
            /*     */// 32: aload_0
            /*     */// 33: getfield 18
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:randomAccessFile
            // Ljava/nio/channels/FileChannel;
            /*     */// 36: lload_1
            /*     */// 37: invokevirtual 62 java/nio/channels/FileChannel:position
            // (J)Ljava/nio/channels/FileChannel;
            /*     */// 40: pop
            /*     */// 41: aload_0
            /*     */// 42: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 45: lload_1
            /*     */// 46: invokevirtual 63
            // com/appdynamics/analytics/agent/input/tail/TailFileState:setLastReadPosition
            // (J)V
            /*     */// 49: aload_0
            /*     */// 50: getfield 8
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:readBuffer
            // [B
            /*     */// 53: invokestatic 64 java/nio/ByteBuffer:wrap
            // ([B)Ljava/nio/ByteBuffer;
            /*     */// 56: astore_1
            /*     */// 57: aload_0
            /*     */// 58: getfield 18
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:randomAccessFile
            // Ljava/nio/channels/FileChannel;
            /*     */// 61: ifnull +512 -> 573
            /*     */// 64: aload_0
            /*     */// 65: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 68: invokevirtual 65
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:isRunning
            // ()Z
            /*     */// 71: ifeq +502 -> 573
            /*     */// 74: iconst_0
            /*     */// 75: istore_2
            /*     */// 76: aload_1
            /*     */// 77: aload_0
            /*     */// 78: getfield 11
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:bufferReadPosition
            // I
            /*     */// 81: invokevirtual 66 java/nio/ByteBuffer:position
            // (I)Ljava/nio/Buffer;
            /*     */// 84: pop
            /*     */// 85: aload_0
            /*     */// 86: getfield 18
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:randomAccessFile
            // Ljava/nio/channels/FileChannel;
            /*     */// 89: aload_1
            /*     */// 90: invokevirtual 67 java/nio/channels/FileChannel:read
            // (Ljava/nio/ByteBuffer;)I
            /*     */// 93: istore_3
            /*     */// 94: iload_3
            /*     */// 95: ifle +17 -> 112
            /*     */// 98: aload_0
            /*     */// 99: dup
            /*     */// 100: getfield 11
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:bufferReadPosition
            // I
            /*     */// 103: iload_3
            /*     */// 104: iadd
            /*     */// 105: putfield 11
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:bufferReadPosition
            // I
            /*     */// 108: aload_0
            /*     */// 109: invokespecial 68
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tryProcessBufferIntoLines
            // ()V
            /*     */// 112: iload_3
            /*     */// 113: istore_2
            /*     */// 114: iload_2
            /*     */// 115: ifgt +82 -> 197
            /*     */// 118: aload_0
            /*     */// 119: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 122: aload_0
            /*     */// 123: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 126: invokestatic 69
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$300
            // (Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;)Lcom/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration;
            /*     */// 129: invokevirtual 70
            // com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration:getTailInterval
            // ()Lcom/appdynamics/common/util/datetime/TimeUnitConfiguration;
            /*     */// 132: invokevirtual 71
            // com/appdynamics/common/util/datetime/TimeUnitConfiguration:toMilliseconds
            // ()J
            /*     */// 135: invokevirtual 72
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:sleepWhileRunning
            // (J)V
            /*     */// 138: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 141: ldc 73
            /*     */// 143: iconst_3
            /*     */// 144: anewarray 38 java/lang/Object
            /*     */// 147: dup
            /*     */// 148: iconst_0
            /*     */// 149: aload_0
            /*     */// 150: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 153: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 156: aastore
            /*     */// 157: dup
            /*     */// 158: iconst_1
            /*     */// 159: aload_0
            /*     */// 160: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 163: invokestatic 69
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$300
            // (Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;)Lcom/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration;
            /*     */// 166: invokevirtual 70
            // com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration:getTailInterval
            // ()Lcom/appdynamics/common/util/datetime/TimeUnitConfiguration;
            /*     */// 169: invokevirtual 74
            // com/appdynamics/common/util/datetime/TimeUnitConfiguration:getTime
            // ()J
            /*     */// 172: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 175: aastore
            /*     */// 176: dup
            /*     */// 177: iconst_2
            /*     */// 178: aload_0
            /*     */// 179: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 182: invokevirtual 26
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getLastReadPosition
            // ()J
            /*     */// 185: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 188: aastore
            /*     */// 189: invokeinterface 75 3 0
            /*     */// 194: goto +376 -> 570
            /*     */// 197: aload_0
            /*     */// 198: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 201: invokestatic 15 java/lang/System:currentTimeMillis ()J
            /*     */// 204: invokevirtual 16
            // com/appdynamics/analytics/agent/input/tail/TailFileState:setLastReadTimestamp
            // (J)V
            /*     */// 207: invokestatic 76 java/lang/Thread:yield ()V
            /*     */// 210: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 213: ldc 77
            /*     */// 215: iconst_3
            /*     */// 216: anewarray 38 java/lang/Object
            /*     */// 219: dup
            /*     */// 220: iconst_0
            /*     */// 221: aload_0
            /*     */// 222: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 225: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 228: aastore
            /*     */// 229: dup
            /*     */// 230: iconst_1
            /*     */// 231: iload_2
            /*     */// 232: invokestatic 78 java/lang/Integer:valueOf
            // (I)Ljava/lang/Integer;
            /*     */// 235: aastore
            /*     */// 236: dup
            /*     */// 237: iconst_2
            /*     */// 238: aload_0
            /*     */// 239: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 242: invokevirtual 26
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getLastReadPosition
            // ()J
            /*     */// 245: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 248: aastore
            /*     */// 249: invokeinterface 79 3 0
            /*     */// 254: goto +316 -> 570
            /*     */// 257: astore_3
            /*     */// 258: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 261: ldc 81
            /*     */// 263: aload_0
            /*     */// 264: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 267: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 270: aload_0
            /*     */// 271: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 274: invokevirtual 39
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getSignature
            // ()Lcom/appdynamics/analytics/agent/input/tail/FileSignature;
            /*     */// 277: invokeinterface 82 4 0
            /*     */// 282: iload_2
            /*     */// 283: ifgt +82 -> 365
            /*     */// 286: aload_0
            /*     */// 287: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 290: aload_0
            /*     */// 291: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 294: invokestatic 69
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$300
            // (Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;)Lcom/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration;
            /*     */// 297: invokevirtual 70
            // com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration:getTailInterval
            // ()Lcom/appdynamics/common/util/datetime/TimeUnitConfiguration;
            /*     */// 300: invokevirtual 71
            // com/appdynamics/common/util/datetime/TimeUnitConfiguration:toMilliseconds
            // ()J
            /*     */// 303: invokevirtual 72
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:sleepWhileRunning
            // (J)V
            /*     */// 306: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 309: ldc 73
            /*     */// 311: iconst_3
            /*     */// 312: anewarray 38 java/lang/Object
            /*     */// 315: dup
            /*     */// 316: iconst_0
            /*     */// 317: aload_0
            /*     */// 318: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 321: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 324: aastore
            /*     */// 325: dup
            /*     */// 326: iconst_1
            /*     */// 327: aload_0
            /*     */// 328: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 331: invokestatic 69
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$300
            // (Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;)Lcom/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration;
            /*     */// 334: invokevirtual 70
            // com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration:getTailInterval
            // ()Lcom/appdynamics/common/util/datetime/TimeUnitConfiguration;
            /*     */// 337: invokevirtual 74
            // com/appdynamics/common/util/datetime/TimeUnitConfiguration:getTime
            // ()J
            /*     */// 340: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 343: aastore
            /*     */// 344: dup
            /*     */// 345: iconst_2
            /*     */// 346: aload_0
            /*     */// 347: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 350: invokevirtual 26
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getLastReadPosition
            // ()J
            /*     */// 353: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 356: aastore
            /*     */// 357: invokeinterface 75 3 0
            /*     */// 362: goto +208 -> 570
            /*     */// 365: aload_0
            /*     */// 366: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 369: invokestatic 15 java/lang/System:currentTimeMillis ()J
            /*     */// 372: invokevirtual 16
            // com/appdynamics/analytics/agent/input/tail/TailFileState:setLastReadTimestamp
            // (J)V
            /*     */// 375: invokestatic 76 java/lang/Thread:yield ()V
            /*     */// 378: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 381: ldc 77
            /*     */// 383: iconst_3
            /*     */// 384: anewarray 38 java/lang/Object
            /*     */// 387: dup
            /*     */// 388: iconst_0
            /*     */// 389: aload_0
            /*     */// 390: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 393: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 396: aastore
            /*     */// 397: dup
            /*     */// 398: iconst_1
            /*     */// 399: iload_2
            /*     */// 400: invokestatic 78 java/lang/Integer:valueOf
            // (I)Ljava/lang/Integer;
            /*     */// 403: aastore
            /*     */// 404: dup
            /*     */// 405: iconst_2
            /*     */// 406: aload_0
            /*     */// 407: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 410: invokevirtual 26
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getLastReadPosition
            // ()J
            /*     */// 413: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 416: aastore
            /*     */// 417: invokeinterface 79 3 0
            /*     */// 422: goto +148 -> 570
            /*     */// 425: astore 4
            /*     */// 427: iload_2
            /*     */// 428: ifgt +82 -> 510
            /*     */// 431: aload_0
            /*     */// 432: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 435: aload_0
            /*     */// 436: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 439: invokestatic 69
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$300
            // (Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;)Lcom/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration;
            /*     */// 442: invokevirtual 70
            // com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration:getTailInterval
            // ()Lcom/appdynamics/common/util/datetime/TimeUnitConfiguration;
            /*     */// 445: invokevirtual 71
            // com/appdynamics/common/util/datetime/TimeUnitConfiguration:toMilliseconds
            // ()J
            /*     */// 448: invokevirtual 72
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:sleepWhileRunning
            // (J)V
            /*     */// 451: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 454: ldc 73
            /*     */// 456: iconst_3
            /*     */// 457: anewarray 38 java/lang/Object
            /*     */// 460: dup
            /*     */// 461: iconst_0
            /*     */// 462: aload_0
            /*     */// 463: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 466: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 469: aastore
            /*     */// 470: dup
            /*     */// 471: iconst_1
            /*     */// 472: aload_0
            /*     */// 473: getfield 2
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:this$0
            // Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;
            /*     */// 476: invokestatic 69
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$300
            // (Lcom/appdynamics/analytics/agent/input/tail/TailLogInput;)Lcom/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration;
            /*     */// 479: invokevirtual 70
            // com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration:getTailInterval
            // ()Lcom/appdynamics/common/util/datetime/TimeUnitConfiguration;
            /*     */// 482: invokevirtual 74
            // com/appdynamics/common/util/datetime/TimeUnitConfiguration:getTime
            // ()J
            /*     */// 485: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 488: aastore
            /*     */// 489: dup
            /*     */// 490: iconst_2
            /*     */// 491: aload_0
            /*     */// 492: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 495: invokevirtual 26
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getLastReadPosition
            // ()J
            /*     */// 498: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 501: aastore
            /*     */// 502: invokeinterface 75 3 0
            /*     */// 507: goto +60 -> 567
            /*     */// 510: aload_0
            /*     */// 511: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 514: invokestatic 15 java/lang/System:currentTimeMillis ()J
            /*     */// 517: invokevirtual 16
            // com/appdynamics/analytics/agent/input/tail/TailFileState:setLastReadTimestamp
            // (J)V
            /*     */// 520: invokestatic 76 java/lang/Thread:yield ()V
            /*     */// 523: invokestatic 22
            // com/appdynamics/analytics/agent/input/tail/TailLogInput:access$100
            // ()Lorg/slf4j/Logger;
            /*     */// 526: ldc 77
            /*     */// 528: iconst_3
            /*     */// 529: anewarray 38 java/lang/Object
            /*     */// 532: dup
            /*     */// 533: iconst_0
            /*     */// 534: aload_0
            /*     */// 535: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 538: invokevirtual 24
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getFilename
            // ()Ljava/lang/String;
            /*     */// 541: aastore
            /*     */// 542: dup
            /*     */// 543: iconst_1
            /*     */// 544: iload_2
            /*     */// 545: invokestatic 78 java/lang/Integer:valueOf
            // (I)Ljava/lang/Integer;
            /*     */// 548: aastore
            /*     */// 549: dup
            /*     */// 550: iconst_2
            /*     */// 551: aload_0
            /*     */// 552: getfield 13
            // com/appdynamics/analytics/agent/input/tail/TailLogInput$LogTailer:tailState
            // Lcom/appdynamics/analytics/agent/input/tail/TailFileState;
            /*     */// 555: invokevirtual 26
            // com/appdynamics/analytics/agent/input/tail/TailFileState:getLastReadPosition
            // ()J
            /*     */// 558: invokestatic 28 java/lang/Long:valueOf
            // (J)Ljava/lang/Long;
            /*     */// 561: aastore
            /*     */// 562: invokeinterface 79 3 0
            /*     */// 567: aload 4
            /*     */// 569: athrow
            /*     */// 570: goto -513 -> 57
            /*     */// 573: return
            /*     */// Line number table:
            /*     */// Java source line #180 -> byte code offset #0
            /*     */// Java source line #181 -> byte code offset #7
            /*     */// Java source line #183 -> byte code offset #27
            /*     */// Java source line #184 -> byte code offset #32
            /*     */// Java source line #185 -> byte code offset #41
            /*     */// Java source line #187 -> byte code offset #49
            /*     */// Java source line #189 -> byte code offset #57
            /*     */// Java source line #190 -> byte code offset #74
            /*     */// Java source line #192 -> byte code offset #76
            /*     */// Java source line #193 -> byte code offset #85
            /*     */// Java source line #194 -> byte code offset #94
            /*     */// Java source line #195 -> byte code offset #98
            /*     */// Java source line #196 -> byte code offset #108
            /*     */// Java source line #198 -> byte code offset #112
            /*     */// Java source line #203 -> byte code offset #114
            /*     */// Java source line #204 -> byte code offset #118
            /*     */// Java source line #205 -> byte code offset #138
            /*     */// Java source line #210 -> byte code offset #197
            /*     */// Java source line #211 -> byte code offset #207
            /*     */// Java source line #212 -> byte code offset #210
            /*     */// Java source line #215 -> byte code offset #254
            /*     */// Java source line #199 -> byte code offset #257
            /*     */// Java source line #200 -> byte code offset #258
            /*     */// Java source line #203 -> byte code offset #282
            /*     */// Java source line #204 -> byte code offset #286
            /*     */// Java source line #205 -> byte code offset #306
            /*     */// Java source line #210 -> byte code offset #365
            /*     */// Java source line #211 -> byte code offset #375
            /*     */// Java source line #212 -> byte code offset #378
            /*     */// Java source line #215 -> byte code offset #422
            /*     */// Java source line #203 -> byte code offset #425
            /*     */// Java source line #204 -> byte code offset #431
            /*     */// Java source line #205 -> byte code offset #451
            /*     */// Java source line #210 -> byte code offset #510
            /*     */// Java source line #211 -> byte code offset #520
            /*     */// Java source line #212 -> byte code offset #523
            /*     */// Java source line #216 -> byte code offset #570
            /*     */// Java source line #217 -> byte code offset #573
            /*     */// Local variable table:
            /*     */// start length slot name signature
            /*     */// 0 574 0 this LogTailer
            /*     */// 31 15 1 readPosition long
            /*     */// 56 34 1 byteBuffer ByteBuffer
            /*     */// 75 470 2 bytesReadSoFar int
            /*     */// 93 20 3 bytesRead int
            /*     */// 257 2 3 e IOException
            /*     */// 425 143 4 localObject Object
            /*     */// Exception table:
            /*     */// from to target type
            /*     */// 76 114 257 java/io/IOException
            /*     */// 76 114 425 finally
            /*     */// 257 282 425 finally
            /*     */// 425 427 425 finally
            /*     */}
        /*     */
    }
    /*     */
}

/*
 * Location:
 * /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent
 * .jar!/com/appdynamics/analytics/agent/input/tail/TailLogInput.class Java
 * compiler version: 7 (51.0) JD-Core Version: 0.7.1
 */
