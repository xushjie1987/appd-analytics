/*    */ package com.appdynamics.analytics.processor.zookeeper.exception;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.exception.UnavailableException;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import org.apache.zookeeper.KeeperException;
/*    */ import org.apache.zookeeper.KeeperException.Code;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ZooKeeperExceptionUtils
/*    */ {
/* 34 */   public static final Set<KeeperException.Code> ZOOKEEPER_UNAVAILABLE_ERROR_CODES = Sets.immutableEnumSet(KeeperException.Code.CONNECTIONLOSS, new KeeperException.Code[] { KeeperException.Code.OPERATIONTIMEOUT, KeeperException.Code.SESSIONMOVED, KeeperException.Code.SESSIONEXPIRED });
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
/*    */ 
/*    */ 
/*    */   public static boolean isTransientException(KeeperException exception)
/*    */   {
/* 49 */     return ZOOKEEPER_UNAVAILABLE_ERROR_CODES.contains(exception.code());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void propagateAsInternalException(KeeperException e)
/*    */     throws Exception
/*    */   {
/* 63 */     Preconditions.checkNotNull(e, "KeeperException can't be null.");
/* 64 */     throw (ZOOKEEPER_UNAVAILABLE_ERROR_CODES.contains(e.code()) ? new UnavailableException(e) : new RuntimeException(e));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/exception/ZooKeeperExceptionUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */