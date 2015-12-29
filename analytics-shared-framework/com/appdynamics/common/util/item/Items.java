/*     */ package com.appdynamics.common.util.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ @ThreadSafe
/*     */ public class Items<ID, X extends Item<? extends ID>>
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(Items.class);
/*     */   
/*     */ 
/*     */   protected final String itemNickName;
/*     */   
/*     */   protected final ConcurrentMap<ID, X> items;
/*     */   
/*     */ 
/*     */   public Items(String itemNickName)
/*     */   {
/*  36 */     this.itemNickName = itemNickName;
/*  37 */     this.items = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<ID> getIds()
/*     */   {
/*  44 */     return ImmutableList.copyOf(this.items.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public X get(ID itemId)
/*     */   {
/*  52 */     return (Item)this.items.get(itemId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<? extends X> getAll()
/*     */   {
/*  59 */     return ImmutableList.copyOf(this.items.values());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(X item)
/*     */   {
/*  67 */     X old = (Item)this.items.putIfAbsent(item.getId(), item);
/*  68 */     if (old != null) {
/*  69 */       throw new ItemPresentException(this.itemNickName + " id [" + item.getId() + "] already exists");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public X upsert(X item)
/*     */   {
/*  79 */     X old = (Item)this.items.put(item.getId(), item);
/*  80 */     if (old == null) {
/*  81 */       log.warn(this.itemNickName + " id [" + item.getId() + "] did not exist");
/*     */     }
/*     */     
/*  84 */     return old;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public X remove(ID itemId)
/*     */   {
/*  93 */     X x = (Item)this.items.remove(itemId);
/*  94 */     if (x == null) {
/*  95 */       throw new ItemNotPresentException(this.itemNickName + " id [" + itemId + "] does not exist");
/*     */     }
/*  97 */     return x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeInstance(X instance)
/*     */   {
/* 105 */     boolean removed = this.items.remove(instance.getId(), instance);
/* 106 */     if (!removed) {
/* 107 */       throw new ItemNotPresentException(this.itemNickName + " id [" + instance.getId() + "] does not exist");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void removeAll()
/*     */   {
/* 115 */     List<? extends X> list = new LinkedList(getAll());
/* 116 */     for (X item : list) {
/* 117 */       removeInstance(item);
/*     */     }
/* 119 */     list.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(ID itemId)
/*     */   {
/* 126 */     return this.items.containsKey(itemId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ItemPresentException
/*     */     extends RuntimeException
/*     */   {
/*     */     ItemPresentException(String message)
/*     */     {
/* 139 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ItemNotPresentException
/*     */     extends RuntimeException
/*     */   {
/*     */     ItemNotPresentException(String message)
/*     */     {
/* 151 */       super();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/item/Items.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */