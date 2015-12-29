/*    */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.validation.constraints.NotNull;
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
/*    */ @Deprecated
/*    */ public class Pair<K, V>
/*    */ {
/*    */   @NotNull
/*    */   K name;
/*    */   @NotNull
/*    */   V value;
/*    */   
/* 27 */   public K getName() { return (K)this.name; }
/* 28 */   public void setName(K name) { this.name = name; }
/*    */   
/*    */ 
/*    */ 
/* 32 */   public V getValue() { return (V)this.value; }
/* 33 */   public void setValue(V value) { this.value = value; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Pair(K name, V value)
/*    */   {
/* 41 */     this.name = name;
/* 42 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <K, V> List<Pair<K, V>> from(Map<K, V> map)
/*    */   {
/* 53 */     List<Pair<K, V>> pairs = new LinkedList();
/* 54 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/* 55 */       Object value = entry.getValue();
/*    */       
/* 57 */       if ((value instanceof Map)) {
/* 58 */         value = from((Map)value);
/*    */       }
/*    */       
/* 61 */       pairs.add(new Pair(entry.getKey(), value));
/*    */     }
/* 63 */     return pairs;
/*    */   }
/*    */   
/*    */   public Pair() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/Pair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */