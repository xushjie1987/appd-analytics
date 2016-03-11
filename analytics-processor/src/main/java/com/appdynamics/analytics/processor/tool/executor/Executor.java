/*     */ package com.appdynamics.analytics.processor.tool.executor;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.FailedResponse;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.var.DottedKeyMapBasedVariableResolver;
/*     */ import com.appdynamics.common.util.var.VariableResolver;
/*     */ import com.appdynamics.common.util.var.Variables;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.io.Console;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.config.RequestConfig.Builder;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
/*     */ public class Executor
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(Executor.class);
/*     */   
/*     */   public static final int GENERAL_SUCCESS_EXIT_CODE = 0;
/*     */   
/*     */   public static final int GENERAL_FAILED_EXIT_CODE = 1;
/*     */   
/*     */   static final String EXECUTOR_STEP_PROPERTIES_CLASS_NAME = "Properties";
/*     */   
/*     */   protected static final String CONSOLE_FEEDBACK_FAILED = "(c)ontinue, (a)bort, (r)etry - (a): ";
/*     */   
/*     */   protected static final String CONSOLE_FEEDBACK_SUCCESSFUL = "(c)ontinue, (a)bort - (a): ";
/*     */   
/*     */   protected static final String CONSOLE_RESPONSE_ABORT = "a";
/*     */   protected static final String CONSOLE_RESPONSE_RETRY = "r";
/*     */   protected static final String CONSOLE_RESPONSE_CONTINUE = "c";
/*     */   public static final String EXECUTOR_STEP_PROPERTIES_FIELD_NAME = "properties";
/*     */   final ExecutionConfig executionConfig;
/*     */   final boolean isInteractive;
/*     */   String startStep;
/*     */   List<ExecutionStep> stepList;
/*     */   
/*     */   public Executor(ExecutionConfig executionConfig, boolean isInteractive, String startStep)
/*     */   {
/*  66 */     this.isInteractive = isInteractive;
/*  67 */     this.startStep = startStep;
/*  68 */     this.executionConfig = executionConfig;
/*  69 */     this.stepList = convertConfigToSteps();
/*     */   }
/*     */   
/*     */ 
/*     */   private List<ExecutionStep> convertConfigToSteps()
/*     */   {
/*  75 */     Preconditions.checkArgument((this.executionConfig.steps != null) && (!this.executionConfig.steps.isEmpty()), "Configuration must specify at least one step");
/*     */     
/*     */ 
/*  78 */     if (Strings.isNullOrEmpty(this.startStep)) {
/*  79 */       this.startStep = ((ConfigStep)this.executionConfig.steps.get(0)).getStepName();
/*     */     }
/*  81 */     List<ExecutionStep> executionSteps = new ArrayList();
/*  82 */     boolean reachedStartStep = false;
/*  83 */     for (ConfigStep configStep : this.executionConfig.steps)
/*     */     {
/*  85 */       if (configStep.getStepName().equals(this.startStep)) {
/*  86 */         reachedStartStep = true;
/*     */       }
/*     */       
/*  89 */       if (reachedStartStep)
/*     */       {
/*     */ 
/*  92 */         ExecutionStep step = buildExecutionStep(configStep.className, configStep.properties, configStep.stepName);
/*     */         
/*  94 */         executionSteps.add(step);
/*     */       }
/*     */     }
/*  97 */     if (!reachedStartStep) {
/*  98 */       log.error("No steps added to queue, likely that the start step name {} was misspelled. Nothing will happen.", this.startStep);
/*     */     }
/*     */     
/* 101 */     return executionSteps;
/*     */   }
/*     */   
/*     */   static ExecutionStep buildExecutionStep(String className, JsonNode properties, String stepName)
/*     */   {
/*     */     ExecutionStep step;
/*     */     try {
/* 108 */       Class<?> stepClass = Class.forName(className);
/* 109 */       Object propertyObject = buildPropertiesObject(stepClass, properties);
/* 110 */       if (propertyObject != null) {
/*     */         try {
/* 112 */           step = (ExecutionStep)stepClass.getConstructor(new Class[] { propertyObject.getClass() }).newInstance(new Object[] { propertyObject });
/*     */         }
/*     */         catch (NoSuchMethodException e) {
/* 115 */           throw new IllegalArgumentException("Step class [" + stepClass.getName() + "] does not have " + "a constructor that takes the Properties class, but it does have a Properties class");
/*     */         }
/*     */         
/*     */       } else {
/* 119 */         step = (ExecutionStep)stepClass.newInstance();
/*     */       }
/* 121 */       step.setName(stepName);
/*     */     }
/*     */     catch (InstantiationException|IllegalAccessException|ClassNotFoundException|InvocationTargetException e) {
/* 124 */       throw Throwables.propagate(e);
/*     */     }
/* 126 */     return step;
/*     */   }
/*     */   
/*     */   private static Object buildPropertiesObject(Class stepClass, JsonNode properties)
/*     */   {
/* 131 */     Class<?>[] classes = stepClass.getDeclaredClasses();
/* 132 */     for (Class klass : classes) {
/* 133 */       if (klass.getSimpleName().equals("Properties")) {
/* 134 */         if (!Modifier.isStatic(klass.getModifiers())) {
/* 135 */           throw new IllegalArgumentException("By convention, executor step classes must have a STATIC inner class called Properties");
/*     */         }
/*     */         
/* 138 */         return Reader.DEFAULT_JSON_MAPPER.convertValue(properties, klass);
/*     */       }
/*     */     }
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public void executeSteps()
/*     */   {
/* 146 */     ExecutionContext executionContext = new ExecutionContext(buildHttpClient());
/*     */     
/* 148 */     for (int i = 0; i < this.stepList.size(); i++) {
/* 149 */       ExecutionStep step = (ExecutionStep)this.stepList.get(i);
/*     */       do {
/* 151 */         log.info("Executing step: " + step.getName());
/* 152 */       } while (executeStepAndRespond(step, executionContext, i == this.stepList.size() - 1));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean executeStepAndRespond(ExecutionStep step, ExecutionContext executionContext, boolean isLastItem)
/*     */   {
/*     */     ExecutionResponse response;
/*     */     try
/*     */     {
/* 161 */       response = step.executeStep(executionContext);
/*     */     } catch (Exception e) {
/* 163 */       response = new FailedResponse(1, e.getMessage() + ":\n" + Throwables.getStackTraceAsString(e));
/*     */     }
/*     */     
/* 166 */     boolean isFailedResponse = response.getClass().equals(FailedResponse.class);
/*     */     
/* 168 */     return processInput(isFailedResponse, step.getName(), response.getResponseMessage(), isLastItem);
/*     */   }
/*     */   
/*     */   private boolean processInput(boolean isFailedResponse, String stepName, String responseMessage, boolean isLastItem)
/*     */   {
/* 173 */     if (isFailedResponse) {
/* 174 */       log.error("Error executing step {}: {}", stepName, responseMessage);
/*     */       
/* 176 */       if (!this.isInteractive) {
/* 177 */         throw new ProcessHaltedException("Interactive mode is not on, so the program is shutting down.");
/*     */       }
/*     */     }
/* 180 */     else if (!isLastItem) {
/* 181 */       log.info("Step [{}] executed successfully:\n{}\nContinuing to next step...", stepName, responseMessage);
/*     */     }
/*     */     
/*     */ 
/* 185 */     if ((!this.isInteractive) || (isLastItem)) {
/* 186 */       return false;
/*     */     }
/* 188 */     Console c = System.console();
/* 189 */     if (c == null) {
/* 190 */       throw new ProcessHaltedException("Program not started from console, so can't perform interactive features");
/*     */     }
/*     */     for (;;) {
/*     */       String input;
/*     */       String input;
/* 195 */       if (isFailedResponse) {
/* 196 */         input = c.readLine("(c)ontinue, (a)bort, (r)etry - (a): ", new Object[0]);
/*     */       } else {
/* 198 */         input = c.readLine("(c)ontinue, (a)bort - (a): ", new Object[0]);
/*     */       }
/*     */       
/* 201 */       if ((Strings.isNullOrEmpty(input)) || (input.toLowerCase().startsWith("a")))
/* 202 */         throw new ProcessHaltedException();
/* 203 */       if (input.toLowerCase().startsWith("r"))
/* 204 */         return true;
/* 205 */       if (input.toLowerCase().startsWith("c")) {
/* 206 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private CloseableHttpClient buildHttpClient()
/*     */   {
/* 213 */     RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(18000000).build();
/*     */     
/*     */ 
/*     */ 
/* 217 */     PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
/* 218 */     cManager.setDefaultMaxPerRoute(20);
/* 219 */     cManager.setMaxTotal(200);
/* 220 */     return HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cManager).build();
/*     */   }
/*     */   
/*     */   public static void filterStepProperties(ExecutionStep step, ExecutionContext executionContext)
/*     */   {
/*     */     try
/*     */     {
/* 227 */       Field field = step.getClass().getDeclaredField("properties");
/* 228 */       field.setAccessible(true);
/* 229 */       Object propertiesObject = field.get(step);
/* 230 */       VariableResolver variableResolver = new DottedKeyMapBasedVariableResolver(executionContext.getProperties(), true);
/*     */       
/* 232 */       Field[] fields = propertiesObject.getClass().getDeclaredFields();
/* 233 */       for (Field objectField : fields) {
/* 234 */         objectField.setAccessible(true);
/* 235 */         filterObject(objectField, propertiesObject, variableResolver);
/*     */       }
/*     */     }
/*     */     catch (NoSuchFieldException|IllegalAccessException ignore) {
/* 239 */       log.debug(ignore.getMessage(), ignore);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void filterObject(Field field, Object object, VariableResolver variableResolver)
/*     */     throws IllegalAccessException
/*     */   {
/* 246 */     if (String.class.isAssignableFrom(field.getType())) {
/* 247 */       String sValue = (String)field.get(object);
/* 248 */       if (sValue != null) {
/* 249 */         field.set(object, Variables.resolve(sValue, variableResolver));
/*     */       }
/* 251 */     } else if (Collection.class.isAssignableFrom(field.getType())) {
/* 252 */       processCollection((Collection)field.get(object), variableResolver);
/* 253 */     } else if (Map.class.isAssignableFrom(field.getType())) {
/* 254 */       processMap((Map)field.get(object), variableResolver);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void processMap(Map<Object, Object> map, VariableResolver variableResolver)
/*     */   {
/* 260 */     if (map != null) {
/* 261 */       for (Map.Entry entry : map.entrySet()) {
/* 262 */         Object value = entry.getValue();
/* 263 */         if ((value != null) && (String.class.isAssignableFrom(value.getClass()))) {
/* 264 */           entry.setValue(Variables.resolve((String)value, variableResolver));
/* 265 */         } else if ((value != null) && (Collection.class.isAssignableFrom(value.getClass()))) {
/* 266 */           processCollection((Collection)value, variableResolver);
/* 267 */         } else if ((value != null) && (Map.class.isAssignableFrom(value.getClass()))) {
/* 268 */           processMap((Map)value, variableResolver);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void processCollection(Collection collection, VariableResolver variableResolver)
/*     */   {
/* 276 */     if (collection != null) {
/*     */       try {
/* 278 */         Collection<Object> newCollection = (Collection)collection.getClass().newInstance();
/* 279 */         for (Object item : collection) {
/* 280 */           if ((item != null) && (String.class.isAssignableFrom(item.getClass()))) {
/* 281 */             item = Variables.resolve((String)item, variableResolver);
/* 282 */           } else if ((item != null) && (Collection.class.isAssignableFrom(item.getClass()))) {
/* 283 */             processCollection((Collection)item, variableResolver);
/* 284 */           } else if ((item != null) && (Map.class.isAssignableFrom(item.getClass()))) {
/* 285 */             processMap((Map)item, variableResolver);
/*     */           }
/* 287 */           newCollection.add(item);
/*     */         }
/* 289 */         collection.clear();
/* 290 */         collection.addAll(newCollection);
/*     */       } catch (Exception e) {
/* 292 */         throw Throwables.propagate(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/Executor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */