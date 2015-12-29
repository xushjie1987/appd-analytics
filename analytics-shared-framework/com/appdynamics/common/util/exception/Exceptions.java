/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.base.Throwables;
/*    */ import java.util.LinkedList;
/*    */ import org.slf4j.helpers.FormattingTuple;
/*    */ import org.slf4j.helpers.MessageFormatter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Exceptions
/*    */ {
/* 18 */   private static final Joiner MESSAGE_JOINER = Joiner.on("\nCaused/related: ").skipNulls();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void rethrowAsRuntimeException(InterruptedException e)
/*    */   {
/* 31 */     rethrowAsRuntimeException(null, e);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void rethrowAsRuntimeException(String optionalMessage, InterruptedException e)
/*    */   {
/* 43 */     Thread.currentThread().interrupt();
/* 44 */     if (optionalMessage != null) {
/* 45 */       throw new RuntimeException(optionalMessage, e);
/*    */     }
/* 47 */     throw new RuntimeException(e);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RuntimeException addSuppressedMsgAndPropagate(Exception e, String format, Object... args)
/*    */   {
/* 58 */     RuntimeException runtime = new RuntimeException(MessageFormatter.arrayFormat(format, args).getMessage());
/* 59 */     e.addSuppressed(runtime);
/* 60 */     return Throwables.propagate(e);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String collectMessages(Throwable t)
/*    */   {
/* 70 */     LinkedList<String> messages = new LinkedList();
/* 71 */     for (Throwable throwable : Throwables.getCausalChain(t)) {
/* 72 */       String s = throwable.getMessage();
/* 73 */       if (!Strings.isNullOrEmpty(s)) {
/* 74 */         messages.add(s);
/*    */       }
/* 76 */       collectSuppressedMessages(throwable, messages);
/*    */     }
/* 78 */     return MESSAGE_JOINER.join(messages);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static void collectSuppressedMessages(Throwable t, LinkedList<String> messages)
/*    */   {
/* 88 */     Throwable[] allSuppressed = t.getSuppressed();
/* 89 */     for (Throwable suppressed : allSuppressed) {
/* 90 */       String s = suppressed.getMessage();
/* 91 */       if (!Strings.isNullOrEmpty(s)) {
/* 92 */         messages.add(s);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/Exceptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */