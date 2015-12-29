/*    */ package com.appdynamics.analytics.shared.rest.client.eventservice.creator;
/*    */ 
/*    */ import com.google.common.base.Throwables;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileBasedEventTypeCreator
/*    */   implements EventTypeCreator
/*    */ {
/*    */   protected final String eventTypeName;
/*    */   protected final String eventTypeMappingPath;
/*    */   protected String eventTypeMapping;
/*    */   
/* 21 */   public String toString() { return "FileBasedEventTypeCreator(eventTypeName=" + getEventTypeName() + ", eventTypeMappingPath=" + this.eventTypeMappingPath + ", eventTypeMapping=" + getEventTypeMapping() + ")"; }
/* 22 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof FileBasedEventTypeCreator)) return false; FileBasedEventTypeCreator other = (FileBasedEventTypeCreator)o; if (!other.canEqual(this)) return false; Object this$eventTypeName = getEventTypeName();Object other$eventTypeName = other.getEventTypeName();return this$eventTypeName == null ? other$eventTypeName == null : this$eventTypeName.equals(other$eventTypeName); } public boolean canEqual(Object other) { return other instanceof FileBasedEventTypeCreator; } public int hashCode() { int PRIME = 31;int result = 1;Object $eventTypeName = getEventTypeName();result = result * 31 + ($eventTypeName == null ? 0 : $eventTypeName.hashCode());return result;
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
/*    */ 
/*    */   public FileBasedEventTypeCreator(String eventTypeName, String eventTypeMappingPath)
/*    */   {
/* 36 */     this.eventTypeName = eventTypeName;
/* 37 */     this.eventTypeMappingPath = eventTypeMappingPath;
/*    */   }
/*    */   
/*    */   public String getEventTypeMapping()
/*    */   {
/* 42 */     if (this.eventTypeMapping != null) {
/* 43 */       return this.eventTypeMapping;
/*    */     }
/*    */     try {
/* 46 */       InputStream is = FileBasedEventTypeCreator.class.getResourceAsStream(this.eventTypeMappingPath);Throwable localThrowable2 = null;
/* 47 */       try { this.eventTypeMapping = IOUtils.toString(is, "UTF-8");
/*    */       }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 46 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*    */       } finally {
/* 48 */         if (is != null) if (localThrowable2 != null) try { is.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else is.close();
/* 49 */       } } catch (IOException e) { Throwables.propagate(e);
/*    */     }
/* 51 */     return this.eventTypeMapping;
/*    */   }
/*    */   
/*    */   public String getEventTypeName()
/*    */   {
/* 56 */     return this.eventTypeName;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/creator/FileBasedEventTypeCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */