/*    */ package com.appdynamics.common.util.misc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Pair<X, Y>
/*    */ {
/*    */   final X left;
/*    */   
/*    */ 
/*    */ 
/*    */   final Y right;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 20 */     if (o == this) return true; if (!(o instanceof Pair)) return false; Pair<?, ?> other = (Pair)o; if (!other.canEqual(this)) return false; Object this$left = getLeft();Object other$left = other.getLeft(); if (this$left == null ? other$left != null : !this$left.equals(other$left)) return false; Object this$right = getRight();Object other$right = other.getRight();return this$right == null ? other$right == null : this$right.equals(other$right); } public boolean canEqual(Object other) { return other instanceof Pair; } public int hashCode() { int PRIME = 31;int result = 1;Object $left = getLeft();result = result * 31 + ($left == null ? 0 : $left.hashCode());Object $right = getRight();result = result * 31 + ($right == null ? 0 : $right.hashCode());return result; }
/*    */   
/* 22 */   public String toString() { return "Pair(left=" + getLeft() + ", right=" + getRight() + ")"; }
/*    */   
/* 24 */   public X getLeft() { return (X)this.left; }
/* 25 */   public Y getRight() { return (Y)this.right; }
/*    */   
/*    */   public Pair(X left, Y right) {
/* 28 */     this.left = left;
/* 29 */     this.right = right;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/misc/Pair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */