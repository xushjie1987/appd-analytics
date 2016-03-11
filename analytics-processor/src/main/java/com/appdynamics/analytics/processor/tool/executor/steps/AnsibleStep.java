/*     */ package com.appdynamics.analytics.processor.tool.executor.steps;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionContext;
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStep;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.text.MessageFormat;
/*     */ import javax.validation.constraints.NotNull;
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
/*     */ public class AnsibleStep
/*     */   extends SystemCommandStep
/*     */ {
/*  48 */   private static final Logger log = LoggerFactory.getLogger(AnsibleStep.class);
/*     */   private final Properties properties;
/*     */   
/*     */   public static class Properties
/*     */   {
/*     */     @NotNull
/*     */     String ansibleCommand;
/*     */     @NotNull
/*     */     String ansibleTag;
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/*  51 */       if (o == this) return true; if (!(o instanceof Properties)) return false; Properties other = (Properties)o; if (!other.canEqual(this)) return false; Object this$ansibleCommand = getAnsibleCommand();Object other$ansibleCommand = other.getAnsibleCommand(); if (this$ansibleCommand == null ? other$ansibleCommand != null : !this$ansibleCommand.equals(other$ansibleCommand)) return false; Object this$ansibleTag = getAnsibleTag();Object other$ansibleTag = other.getAnsibleTag(); if (this$ansibleTag == null ? other$ansibleTag != null : !this$ansibleTag.equals(other$ansibleTag)) return false; Object this$ansibleInventoryFile = getAnsibleInventoryFile();Object other$ansibleInventoryFile = other.getAnsibleInventoryFile(); if (this$ansibleInventoryFile == null ? other$ansibleInventoryFile != null : !this$ansibleInventoryFile.equals(other$ansibleInventoryFile)) return false; Object this$ansibleUser = getAnsibleUser();Object other$ansibleUser = other.getAnsibleUser(); if (this$ansibleUser == null ? other$ansibleUser != null : !this$ansibleUser.equals(other$ansibleUser)) return false; Object this$ansiblePrivateKey = getAnsiblePrivateKey();Object other$ansiblePrivateKey = other.getAnsiblePrivateKey();return this$ansiblePrivateKey == null ? other$ansiblePrivateKey == null : this$ansiblePrivateKey.equals(other$ansiblePrivateKey);
/*     */     }
/*     */     @NotNull
/*     */     String ansibleInventoryFile;
/*     */     
/*     */     public boolean canEqual(Object other)
/*     */     {
/*  51 */       return other instanceof Properties;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  51 */       int PRIME = 31;int result = 1;Object $ansibleCommand = getAnsibleCommand();result = result * 31 + ($ansibleCommand == null ? 0 : $ansibleCommand.hashCode());Object $ansibleTag = getAnsibleTag();result = result * 31 + ($ansibleTag == null ? 0 : $ansibleTag.hashCode());Object $ansibleInventoryFile = getAnsibleInventoryFile();result = result * 31 + ($ansibleInventoryFile == null ? 0 : $ansibleInventoryFile.hashCode());Object $ansibleUser = getAnsibleUser();result = result * 31 + ($ansibleUser == null ? 0 : $ansibleUser.hashCode());Object $ansiblePrivateKey = getAnsiblePrivateKey();result = result * 31 + ($ansiblePrivateKey == null ? 0 : $ansiblePrivateKey.hashCode());return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  51 */       return "AnsibleStep.Properties(ansibleCommand=" + getAnsibleCommand() + ", ansibleTag=" + getAnsibleTag() + ", ansibleInventoryFile=" + getAnsibleInventoryFile() + ", ansibleUser=" + getAnsibleUser() + ", ansiblePrivateKey=" + getAnsiblePrivateKey() + ")";
/*     */     }
/*     */     @NotNull
/*     */     String ansibleUser;
/*     */     
/*     */     @ConstructorProperties({"ansibleCommand", "ansibleTag", "ansibleInventoryFile", "ansibleUser", "ansiblePrivateKey"})
/*     */     public Properties(String ansibleCommand, String ansibleTag, String ansibleInventoryFile, String ansibleUser, String ansiblePrivateKey)
/*     */     {
/*  53 */       this.ansibleCommand = ansibleCommand;this.ansibleTag = ansibleTag;this.ansibleInventoryFile = ansibleInventoryFile;this.ansibleUser = ansibleUser;this.ansiblePrivateKey = ansiblePrivateKey;
/*     */     }
/*     */     
/*     */     public String getAnsibleCommand()
/*     */     {
/*  56 */       return this.ansibleCommand;
/*     */     }
/*     */     
/*     */     public void setAnsibleCommand(String ansibleCommand)
/*     */     {
/*  56 */       this.ansibleCommand = ansibleCommand;
/*     */     }
/*     */     
/*     */     public String getAnsibleTag()
/*     */     {
/*  58 */       return this.ansibleTag;
/*     */     }
/*     */     
/*     */     public void setAnsibleTag(String ansibleTag)
/*     */     {
/*  58 */       this.ansibleTag = ansibleTag;
/*     */     }
/*     */     
/*     */     public String getAnsibleInventoryFile()
/*     */     {
/*  60 */       return this.ansibleInventoryFile;
/*     */     }
/*     */     
/*     */     public void setAnsibleInventoryFile(String ansibleInventoryFile)
/*     */     {
/*  60 */       this.ansibleInventoryFile = ansibleInventoryFile;
/*     */     }
/*     */     
/*     */     public String getAnsibleUser()
/*     */     {
/*  62 */       return this.ansibleUser;
/*     */     }
/*     */     
/*     */     public void setAnsibleUser(String ansibleUser)
/*     */     {
/*  62 */       this.ansibleUser = ansibleUser;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     String ansiblePrivateKey;
/*     */     public String getAnsiblePrivateKey()
/*     */     {
/*  64 */       return this.ansiblePrivateKey;
/*     */     }
/*     */     
/*     */     public void setAnsiblePrivateKey(String ansiblePrivateKey)
/*     */     {
/*  64 */       this.ansiblePrivateKey = ansiblePrivateKey;
/*     */     }
/*     */     
/*     */     public Properties copy()
/*     */     {
/*  67 */       return new Properties(this.ansibleCommand, this.ansibleTag, this.ansibleInventoryFile, this.ansibleUser, this.ansiblePrivateKey);
/*     */     }
/*     */     
/*     */     public Properties() {}
/*     */   }
/*     */   
/*     */   public AnsibleStep(Properties properties)
/*     */   {
/*  75 */     Preconditions.checkArgument((properties != null) && (!Strings.isNullOrEmpty(properties.ansibleCommand)) && (!Strings.isNullOrEmpty(properties.ansibleTag)) && (!Strings.isNullOrEmpty(properties.ansibleInventoryFile)) && (!Strings.isNullOrEmpty(properties.ansibleUser)) && (!Strings.isNullOrEmpty(properties.ansiblePrivateKey)), "Expected properties to be non-null and to have ansibleCommand, ansibleTag, ansibleInventoryFile, ansibleUser, and ansiblePrivateKey set");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   protected Logger log()
/*     */   {
/*  87 */     return log;
/*     */   }
/*     */   
/*     */   public ExecutionResponse executeStep(ExecutionContext executionContext)
/*     */   {
/*  92 */     this.properties = new SystemCommandStep.Properties(MessageFormat.format("ansible -i {0} -u{1} --private-key={2} -l {3} -mcommand -a\"{4}\"", new Object[] { this.properties.ansibleInventoryFile, this.properties.ansibleUser, this.properties.ansiblePrivateKey, this.properties.ansibleTag, this.properties.ansibleCommand }));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */     return super.executeStep(executionContext);
/*     */   }
/*     */   
/*     */   public ExecutionStep copy()
/*     */   {
/* 103 */     AnsibleStep step = new AnsibleStep(this.properties.copy());
/* 104 */     step.setName(getName());
/* 105 */     return step;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/steps/AnsibleStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */