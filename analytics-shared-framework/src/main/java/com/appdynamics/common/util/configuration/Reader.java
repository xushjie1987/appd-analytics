/*     */ package com.appdynamics.common.util.configuration;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
/*     */ import com.fasterxml.jackson.datatype.guava.GuavaModule;
/*     */ import com.fasterxml.jackson.datatype.joda.JodaModule;
/*     */ import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import io.dropwizard.jackson.AnnotationSensitivePropertyNamingStrategy;
/*     */ import io.dropwizard.jackson.DiscoverableSubtypeResolver;
/*     */ import io.dropwizard.jackson.FuzzyEnumModule;
/*     */ import io.dropwizard.jackson.GuavaExtrasModule;
/*     */ import io.dropwizard.jackson.LogbackModule;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
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
/*     */ public final class Reader
/*     */ {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(Reader.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   private static final YAMLFactory YAML_FACTORY = new YAMLFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   public static final ObjectMapper DEFAULT_YAML_MAPPER = newYamlObjectMapper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   public static final ObjectMapper DEFAULT_JSON_MAPPER = newJsonObjectMapper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T readFrom(Class<T> klass, InputStream source)
/*     */     throws IOException
/*     */   {
/*  69 */     Objects.requireNonNull(source, "Source cannot be null");
/*  70 */     T value = DEFAULT_YAML_MAPPER.readValue(source, klass);
/*  71 */     validate(value);
/*     */     
/*  73 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T readFrom(TypeToken<T> typeReference, InputStream source)
/*     */   {
/*  86 */     Objects.requireNonNull(source, "Source cannot be null");
/*     */     
/*  88 */     T value = null;
/*     */     try
/*     */     {
/*  91 */       TypeReference<T> tr = new TypeReference()
/*     */       {
/*     */         public Type getType() {
/*  94 */           return this.val$typeReference.getType();
/*     */         }
/*     */         
/*  97 */       };
/*  98 */       value = DEFAULT_YAML_MAPPER.readValue(source, tr);
/*     */     } catch (IOException|IllegalArgumentException e) {
/* 100 */       throw new ConfigurationException(e.getMessage(), e);
/*     */     }
/* 102 */     validate(value);
/*     */     
/* 104 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T readFrom(Class<T> klass, Object intermediateSource)
/*     */   {
/* 117 */     Objects.requireNonNull(intermediateSource, "Source cannot be null");
/*     */     
/* 119 */     T value = null;
/*     */     try {
/* 121 */       value = DEFAULT_YAML_MAPPER.convertValue(intermediateSource, klass);
/*     */     } catch (IllegalArgumentException e) {
/* 123 */       throw new ConfigurationException(e.getMessage(), e);
/*     */     }
/* 125 */     validate(value);
/*     */     
/* 127 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T readFrom(TypeToken<T> typeReference, Object intermediateSource)
/*     */   {
/* 140 */     Objects.requireNonNull(intermediateSource, "Source cannot be null");
/*     */     
/* 142 */     T value = null;
/*     */     try
/*     */     {
/* 145 */       TypeReference<T> tr = new TypeReference()
/*     */       {
/*     */         public Type getType() {
/* 148 */           return this.val$typeReference.getType();
/*     */         }
/*     */         
/* 151 */       };
/* 152 */       value = DEFAULT_YAML_MAPPER.convertValue(intermediateSource, tr);
/*     */     } catch (IllegalArgumentException e) {
/* 154 */       throw new ConfigurationException(e.getMessage(), e);
/*     */     }
/* 156 */     validate(value);
/*     */     
/* 158 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ObjectMapper newObjectMapper(JsonFactory factory)
/*     */   {
/*     */     ObjectMapper mapper;
/*     */     
/*     */     ObjectMapper mapper;
/*     */     
/* 169 */     if (factory != null) {
/* 170 */       mapper = new ObjectMapper(factory);
/*     */     } else {
/* 172 */       mapper = new ObjectMapper();
/*     */     }
/* 174 */     mapper.registerModule(new GuavaModule());
/* 175 */     mapper.registerModule(new LogbackModule());
/* 176 */     mapper.registerModule(new GuavaExtrasModule());
/* 177 */     mapper.registerModule(new JodaModule());
/* 178 */     mapper.registerModule(new AfterburnerModule());
/* 179 */     mapper.registerModule(new FuzzyEnumModule());
/* 180 */     mapper.setPropertyNamingStrategy(new AnnotationSensitivePropertyNamingStrategy());
/* 181 */     mapper.setSubtypeResolver(new DiscoverableSubtypeResolver());
/* 182 */     mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
/* 183 */     return mapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ObjectMapper newJsonObjectMapper()
/*     */   {
/* 192 */     return newObjectMapper(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ObjectMapper newYamlObjectMapper()
/*     */   {
/* 201 */     return newObjectMapper(YAML_FACTORY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> void validate(T config)
/*     */   {
/* 210 */     Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
/*     */     
/* 212 */     if (config == null)
/*     */     {
/* 214 */       return;
/*     */     }
/*     */     
/* 217 */     Set<ConstraintViolation<T>> errors = null;
/*     */     try {
/* 219 */       errors = validator.validate(config, new Class[0]);
/*     */     } catch (RuntimeException e) {
/* 221 */       throw new ConfigurationException(e.getMessage(), e);
/*     */     }
/*     */     
/* 224 */     if (!errors.isEmpty()) {
/* 225 */       StringBuilder sb = new StringBuilder("Error occurred while validating instance of [").append(config.getClass().getName()).append("] with config \n [").append(config.toString());
/*     */       
/*     */ 
/*     */ 
/* 229 */       for (ConstraintViolation<T> violation : errors) {
/* 230 */         sb.append("    property '").append(violation.getPropertyPath()).append("' ").append(violation.getMessage()).append(". Found value '").append(violation.getInvalidValue()).append("'.").append("\n");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */       throw new ConfigurationException(sb.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public static String toYaml(Object anything) {
/*     */     try {
/* 246 */       String s = DEFAULT_YAML_MAPPER.writeValueAsString(anything);
/*     */       
/* 248 */       String startingComment = "---\n";
/*     */       
/* 250 */       return s.startsWith(startingComment) ? s.substring(startingComment.length()) : s;
/*     */     } catch (JsonProcessingException e) {
/* 252 */       log.warn("Error occurred while transforming the object to a formatted YML string [" + anything + "]", e); }
/* 253 */     return Parameters.asString(anything, "<unavailable>");
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/Reader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */