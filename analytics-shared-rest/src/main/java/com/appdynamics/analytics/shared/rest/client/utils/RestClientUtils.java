/*     */ package com.appdynamics.analytics.shared.rest.client.utils;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.ClientException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestExceptionFactory;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestExceptionPayload;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClients;
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
/*     */ public final class RestClientUtils
/*     */ {
/*  34 */   static final Joiner URL_JOINER = Joiner.on("/");
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
/*     */   public static void resolve(HttpResponse response, int expectedStatus, ObjectMapper mapper)
/*     */   {
/*  47 */     resolve(response, expectedStatus, mapper, Void.class);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T resolve(HttpResponse response, int expectedStatus, ObjectMapper mapper, Class<T> entityClass)
/*     */   {
/*  63 */     int statusCode = response.getStatusLine().getStatusCode();
/*     */     
/*  65 */     validateResponse(response, expectedStatus, mapper, statusCode);
/*     */     
/*  67 */     if ((response.getEntity() != null) && (entityClass != null) && (!entityClass.equals(Void.class))) {
/*     */       try {
/*  69 */         InputStream responseInput = response.getEntity().getContent();Throwable localThrowable2 = null;
/*  70 */         try { Object localObject1; if (entityClass.isAssignableFrom(String.class)) {
/*  71 */             return (T)entityClass.cast(new String(ByteStreams.toByteArray(responseInput), Charsets.UTF_8));
/*     */           }
/*     */           
/*  74 */           return (T)mapper.reader(entityClass).readValue(responseInput);
/*     */         }
/*     */         catch (Throwable localThrowable3)
/*     */         {
/*  69 */           localThrowable2 = localThrowable3;throw localThrowable3;
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/*  76 */           if (responseInput != null) { if (localThrowable2 != null) try { responseInput.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else { responseInput.close();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  82 */         return null;
/*     */       }
/*     */       catch (IOException e1)
/*     */       {
/*  77 */         throw new ClientException(e1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void validateResponse(HttpResponse response, int expectedStatus, ObjectMapper mapper, int statusCode)
/*     */   {
/*  91 */     if (isError(statusCode))
/*     */     {
/*  93 */       if (response.getEntity() != null) {
/*  94 */         try { InputStream responseInput = response.getEntity().getContent();Throwable localThrowable2 = null;
/*  95 */           try { byte[] data = IOUtils.toByteArray(responseInput);
/*  96 */             if (data.length > 0) {
/*     */               RestExceptionPayload excData;
/*     */               try {
/*  99 */                 excData = (RestExceptionPayload)mapper.reader(RestExceptionPayload.class).readValue(data);
/*     */               }
/*     */               catch (Exception e) {
/* 102 */                 throw RestExceptionFactory.makeException(new RestExceptionPayload(statusCode, "Unknown", "", new String(data, "UTF-8")));
/*     */               }
/*     */               
/* 105 */               excData.setStatusCode(statusCode);
/* 106 */               throw RestExceptionFactory.makeException(excData);
/*     */             }
/* 108 */             throw RestExceptionFactory.makeUnexpectedResponseException(response);
/*     */           }
/*     */           catch (Throwable localThrowable1)
/*     */           {
/*  94 */             localThrowable1 = 
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
/* 108 */               localThrowable1;localThrowable2 = localThrowable1;throw localThrowable1;
/*     */           } finally {
/* 110 */             localObject = finally; if (responseInput != null) if (localThrowable2 != null) try { responseInput.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else responseInput.close(); throw ((Throwable)localObject);
/* 111 */           } } catch (IOException e1) { throw new ClientException(e1);
/*     */         }
/*     */       }
/*     */       
/* 115 */       throw RestExceptionFactory.makeException(new RestExceptionPayload(statusCode, "Unknown", "", "Not Available"));
/*     */     }
/*     */     
/*     */ 
/* 119 */     if ((expectedStatus > 0) && (statusCode != expectedStatus))
/*     */     {
/* 121 */       throw RestExceptionFactory.makeUnexpectedResponseException(response);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isError(int statusCode)
/*     */   {
/* 132 */     return statusCode >= 400;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CloseableHttpClient buildClient()
/*     */   {
/* 140 */     return HttpClients.createDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String buildPath(String firstSegment, String secondSegment, String... pathSegments)
/*     */   {
/* 151 */     return URL_JOINER.join(firstSegment, secondSegment, (Object[])pathSegments);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/RestClientUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */