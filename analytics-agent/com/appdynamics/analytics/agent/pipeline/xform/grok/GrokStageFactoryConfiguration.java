/*    */ package com.appdynamics.analytics.agent.pipeline.xform.grok;
/*    */ 
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GrokStageFactoryConfiguration
/*    */   extends FilePathConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String source;
/*    */   
/* 19 */   public String toString() { return "GrokStageFactoryConfiguration(source=" + getSource() + ")"; }
/* 20 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof GrokStageFactoryConfiguration)) return false; GrokStageFactoryConfiguration other = (GrokStageFactoryConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$source = getSource();Object other$source = other.getSource();return this$source == null ? other$source == null : this$source.equals(other$source); } public boolean canEqual(Object other) { return other instanceof GrokStageFactoryConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $source = getSource();result = result * 31 + ($source == null ? 0 : $source.hashCode());return result;
/*    */   }
/*    */   
/* 23 */   public String getSource() { return this.source; } public void setSource(String source) { this.source = source; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/grok/GrokStageFactoryConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */