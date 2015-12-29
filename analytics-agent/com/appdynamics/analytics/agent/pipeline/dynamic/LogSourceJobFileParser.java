/*     */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.github.mustachejava.DefaultMustacheFactory;
/*     */ import com.github.mustachejava.Mustache;
/*     */ import com.github.mustachejava.MustacheFactory;
/*     */ import com.google.common.base.Charsets;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogSourceJobFileParser
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(LogSourceJobFileParser.class);
/*     */   
/*     */ 
/*     */   private static final int JOB_CONFIG_VERSION_CURRENT = 1;
/*     */   
/*     */ 
/*     */   final String jobFilesDirPath;
/*     */   
/*     */ 
/*     */   final String templatePath;
/*     */   
/*     */   final Mustache mustache;
/*     */   
/*     */   final Properties variableValues;
/*     */   
/*     */ 
/*     */   public LogSourceJobFileParser(String jobFilesDirPath, String mustacheTemplatePath, Properties variableValues)
/*     */   {
/*  44 */     this.jobFilesDirPath = jobFilesDirPath;
/*  45 */     this.templatePath = mustacheTemplatePath;
/*  46 */     MustacheFactory mustacheFactory = new DefaultMustacheFactory();
/*  47 */     this.mustache = mustacheFactory.compile(mustacheTemplatePath);
/*  48 */     this.variableValues = variableValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PipelineConfiguration parsePipelineConfigurationFromFileName(String jobFileName)
/*     */     throws IOException
/*     */   {
/*  60 */     File file = new File(this.jobFilesDirPath, jobFileName);
/*  61 */     return parsePipelineConfigurationFromFile(file);
/*     */   }
/*     */   
/*     */   public PipelineConfiguration parsePipelineConfigurationFromFile(File input) throws IOException {
/*  65 */     InputStream inputStream = null;
/*     */     try
/*     */     {
/*  68 */       TemplateConfiguration templateConfiguration = tryParseTemplateConfiguration(input);
/*  69 */       log.trace("File [{}] appears to be a valid input that can be used against the template [{}]", input.getAbsolutePath(), this.templatePath);
/*     */       
/*  71 */       String yml = createYmlFromTemplate(input, templateConfiguration);
/*  72 */       inputStream = new ByteArrayInputStream(yml.getBytes(Charsets.UTF_8));
/*     */     } catch (IOException e) {
/*  74 */       log.warn("File [" + input.getAbsolutePath() + "] could not be parsed as 'Job DSL' input to" + " [" + this.templatePath + "]. An attempt will be made to read it as a plain YML file", e);
/*     */     }
/*     */     
/*     */ 
/*  78 */     if (inputStream == null) {
/*  79 */       inputStream = new FileInputStream(input);
/*     */     }
/*     */     String varResolvedInput;
/*     */     try
/*     */     {
/*  84 */       varResolvedInput = readAndResolveVariables(input, inputStream);
/*     */     } finally {
/*  86 */       IOUtils.closeQuietly(inputStream);
/*     */     }
/*     */     
/*     */ 
/*  90 */     PipelineConfiguration pipelineConfiguration = tryParsePipelineConfiguration(input, varResolvedInput);
/*  91 */     return pipelineConfiguration;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   String readAndResolveVariables(File originalFile, InputStream unresolvedInputStream)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 8	com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser:variableValues	Ljava/util/Properties;
/*     */     //   4: ldc 37
/*     */     //   6: aload_0
/*     */     //   7: aload_1
/*     */     //   8: invokevirtual 38	com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser:makePipelineId	(Ljava/io/File;)Ljava/lang/String;
/*     */     //   11: invokevirtual 39	java/util/Properties:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   14: pop
/*     */     //   15: new 40	java/io/ByteArrayOutputStream
/*     */     //   18: dup
/*     */     //   19: invokespecial 41	java/io/ByteArrayOutputStream:<init>	()V
/*     */     //   22: astore_3
/*     */     //   23: aconst_null
/*     */     //   24: astore 4
/*     */     //   26: aload_2
/*     */     //   27: aload_0
/*     */     //   28: getfield 8	com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser:variableValues	Ljava/util/Properties;
/*     */     //   31: aload_3
/*     */     //   32: invokestatic 42	com/appdynamics/common/util/var/Variables:resolveVariables	(Ljava/io/InputStream;Ljava/util/Properties;Ljava/io/OutputStream;)V
/*     */     //   35: getstatic 13	com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser:log	Lorg/slf4j/Logger;
/*     */     //   38: ldc 43
/*     */     //   40: aload_1
/*     */     //   41: invokevirtual 15	java/io/File:getAbsolutePath	()Ljava/lang/String;
/*     */     //   44: invokeinterface 44 3 0
/*     */     //   49: new 45	java/lang/String
/*     */     //   52: dup
/*     */     //   53: aload_3
/*     */     //   54: invokevirtual 46	java/io/ByteArrayOutputStream:toByteArray	()[B
/*     */     //   57: getstatic 19	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   60: invokespecial 47	java/lang/String:<init>	([BLjava/nio/charset/Charset;)V
/*     */     //   63: astore 5
/*     */     //   65: aload_3
/*     */     //   66: ifnull +31 -> 97
/*     */     //   69: aload 4
/*     */     //   71: ifnull +22 -> 93
/*     */     //   74: aload_3
/*     */     //   75: invokevirtual 48	java/io/ByteArrayOutputStream:close	()V
/*     */     //   78: goto +19 -> 97
/*     */     //   81: astore 6
/*     */     //   83: aload 4
/*     */     //   85: aload 6
/*     */     //   87: invokevirtual 50	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   90: goto +7 -> 97
/*     */     //   93: aload_3
/*     */     //   94: invokevirtual 48	java/io/ByteArrayOutputStream:close	()V
/*     */     //   97: aload_0
/*     */     //   98: getfield 8	com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser:variableValues	Ljava/util/Properties;
/*     */     //   101: ldc 37
/*     */     //   103: invokevirtual 51	java/util/Properties:remove	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   106: pop
/*     */     //   107: aload 5
/*     */     //   109: areturn
/*     */     //   110: astore 5
/*     */     //   112: aload 5
/*     */     //   114: astore 4
/*     */     //   116: aload 5
/*     */     //   118: athrow
/*     */     //   119: astore 7
/*     */     //   121: aload_3
/*     */     //   122: ifnull +31 -> 153
/*     */     //   125: aload 4
/*     */     //   127: ifnull +22 -> 149
/*     */     //   130: aload_3
/*     */     //   131: invokevirtual 48	java/io/ByteArrayOutputStream:close	()V
/*     */     //   134: goto +19 -> 153
/*     */     //   137: astore 8
/*     */     //   139: aload 4
/*     */     //   141: aload 8
/*     */     //   143: invokevirtual 50	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   146: goto +7 -> 153
/*     */     //   149: aload_3
/*     */     //   150: invokevirtual 48	java/io/ByteArrayOutputStream:close	()V
/*     */     //   153: aload 7
/*     */     //   155: athrow
/*     */     //   156: astore 9
/*     */     //   158: aload_0
/*     */     //   159: getfield 8	com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser:variableValues	Ljava/util/Properties;
/*     */     //   162: ldc 37
/*     */     //   164: invokevirtual 51	java/util/Properties:remove	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   167: pop
/*     */     //   168: aload 9
/*     */     //   170: athrow
/*     */     // Line number table:
/*     */     //   Java source line #96	-> byte code offset #0
/*     */     //   Java source line #97	-> byte code offset #15
/*     */     //   Java source line #98	-> byte code offset #26
/*     */     //   Java source line #99	-> byte code offset #35
/*     */     //   Java source line #101	-> byte code offset #49
/*     */     //   Java source line #102	-> byte code offset #65
/*     */     //   Java source line #103	-> byte code offset #97
/*     */     //   Java source line #97	-> byte code offset #110
/*     */     //   Java source line #102	-> byte code offset #119
/*     */     //   Java source line #103	-> byte code offset #156
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	171	0	this	LogSourceJobFileParser
/*     */     //   0	171	1	originalFile	File
/*     */     //   0	171	2	unresolvedInputStream	InputStream
/*     */     //   22	128	3	outputStream	java.io.ByteArrayOutputStream
/*     */     //   24	116	4	localThrowable2	Throwable
/*     */     //   63	45	5	str	String
/*     */     //   110	7	5	localThrowable1	Throwable
/*     */     //   110	7	5	localThrowable3	Throwable
/*     */     //   81	5	6	x2	Throwable
/*     */     //   119	35	7	localObject1	Object
/*     */     //   137	5	8	x2	Throwable
/*     */     //   156	13	9	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   74	78	81	java/lang/Throwable
/*     */     //   26	65	110	java/lang/Throwable
/*     */     //   26	65	119	finally
/*     */     //   110	121	119	finally
/*     */     //   130	134	137	java/lang/Throwable
/*     */     //   15	97	156	finally
/*     */     //   110	158	156	finally
/*     */   }
/*     */   
/*     */   private String createYmlFromTemplate(File originalFile, TemplateConfiguration templateInput)
/*     */     throws IOException
/*     */   {
/* 116 */     StringWriter transformedYmlWriter = new StringWriter();Throwable localThrowable2 = null;
/* 117 */     String transformedYml; try { this.mustache.execute(transformedYmlWriter, templateInput).flush();
/* 118 */       transformedYml = transformedYmlWriter.toString();
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 116 */       localThrowable2 = localThrowable1;throw localThrowable1;
/*     */     }
/*     */     finally {
/* 119 */       if (transformedYmlWriter != null) if (localThrowable2 != null) try { transformedYmlWriter.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else transformedYmlWriter.close(); }
/* 120 */     log.debug("File [{}] was used to produce YML \n{}", originalFile.getAbsolutePath(), transformedYml);
/* 121 */     return transformedYml;
/*     */   }
/*     */   
/*     */   String makePipelineId(File unresolvedInput) {
/* 125 */     return unresolvedInput.getName();
/*     */   }
/*     */   
/*     */   static TemplateConfiguration tryParseTemplateConfiguration(File input) throws IOException {
/* 129 */     DslConfiguration dsl = (DslConfiguration)Reader.DEFAULT_YAML_MAPPER.readValue(input, DslConfiguration.class);
/*     */     
/* 131 */     Reader.validate(dsl);
/*     */     
/* 133 */     if (dsl.getVersion() != 1) {
/* 134 */       throw new IllegalStateException(String.format("The current job configuration version is [%s] but the job configuration file seems to be from version [%s]", new Object[] { Integer.valueOf(1), Integer.valueOf(dsl.getVersion()) }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 139 */     TemplateConfiguration tc = TemplateConfiguration.from(dsl);
/* 140 */     Reader.validate(tc);
/* 141 */     return tc;
/*     */   }
/*     */   
/*     */   static PipelineConfiguration tryParsePipelineConfiguration(File originalFile, String ymlInput)
/*     */     throws IOException
/*     */   {
/* 147 */     PipelineConfiguration pc = (PipelineConfiguration)Reader.DEFAULT_YAML_MAPPER.readValue(ymlInput, PipelineConfiguration.class);
/* 148 */     Reader.validate(pc);
/* 149 */     log.debug("File [{}] was used to create a valid [{}] \n{}", new Object[] { originalFile.getAbsolutePath(), pc.getClass().getSimpleName(), ymlInput });
/*     */     
/* 151 */     return pc;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */