/*     */ package com.appdynamics.analytics.pipeline.http;
/*     */ 
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.google.common.io.BaseEncoding;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLContextBuilder;
/*     */ import org.apache.http.conn.ssl.SSLContexts;
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
/*     */ public class HttpSecurityModule
/*     */   extends Module<HttpSecurityConfiguration>
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(HttpSecurityModule.class);
/*     */   
/*     */   public static final String PROTOCOL_HTTP = "http";
/*     */   public static final String PROTOCOL_HTTPS = "https";
/*     */   public static final String TRUST_STORE_TYPE = "JKS";
/*     */   
/*     */   public void configure()
/*     */   {
/*  51 */     super.configure();
/*     */     try
/*     */     {
/*  54 */       configureTransport();
/*  55 */       bind(HttpClientContextFactory.class).toInstance(DefaultHttpClientContextFactory.INSTANCE);
/*  56 */       log.debug("HTTP has not been configured to use authentication");
/*     */     }
/*     */     catch (KeyStoreException|IOException|NoSuchAlgorithmException|CertificateException|KeyManagementException e) {
/*  59 */       throw new ConfigurationException("Error occurred while attempting to use the trust store to setup HTTPS connections", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static char[] base64ToClearAscii(String optBase64String)
/*     */   {
/*  65 */     if ((optBase64String == null) || (optBase64String.length() == 0)) {
/*  66 */       return null;
/*     */     }
/*     */     
/*  69 */     byte[] clearTxtPassword = BaseEncoding.base64().decode(optBase64String);
/*  70 */     return new String(clearTxtPassword, StandardCharsets.US_ASCII).toCharArray();
/*     */   }
/*     */   
/*     */   private static void clearArray(char[] chars) {
/*  74 */     if (chars != null)
/*     */     {
/*  76 */       for (int i = 0; i < chars.length; i++) {
/*  77 */         chars[i] = '\000';
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureTransport()
/*     */     throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException
/*     */   {
/*  85 */     RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory());
/*     */     
/*     */ 
/*     */ 
/*  89 */     configureHttps(builder);
/*     */     
/*  91 */     Registry<ConnectionSocketFactory> socketFactoryRegistry = builder.build();
/*  92 */     bind(new TypeLiteral() {}).toInstance(socketFactoryRegistry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void configureHttps(RegistryBuilder<ConnectionSocketFactory> builder)
/*     */     throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException
/*     */   {
/* 101 */     HttpSecurityConfiguration.TrustStoreConfiguration trustStore = ((HttpSecurityConfiguration)getConfiguration()).getTrustStore();
/* 102 */     SSLContext sslContext; SSLContext sslContext; if (trustStore == null) {
/* 103 */       log.debug("Trust store has not been configured for HTTPS");
/* 104 */       sslContext = SSLContext.getDefault();
/*     */     } else {
/* 106 */       sslContext = buildSslContextFromTrustStore(trustStore);
/*     */     }
/*     */     
/* 109 */     bind(SSLContext.class).toInstance(sslContext);
/*     */     
/* 111 */     builder.register("https", new SSLConnectionSocketFactory(sslContext));
/*     */   }
/*     */   
/*     */   private SSLContext buildSslContextFromTrustStore(HttpSecurityConfiguration.TrustStoreConfiguration trustStore) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException
/*     */   {
/* 116 */     KeyStore jks = KeyStore.getInstance("JKS");
/*     */     
/* 118 */     String trustStoreFile = trustStore.getFile();
/*     */     
/* 120 */     String base64Password = trustStore.getPassword();
/* 121 */     char[] password = null;
/* 122 */     if (base64Password != null) {
/* 123 */       password = base64ToClearAscii(base64Password);
/*     */       
/* 125 */       trustStore.setPassword("xxx");
/*     */     }
/*     */     
/* 128 */     InputStream trustStoreStream = new FileInputStream(trustStoreFile);Throwable localThrowable2 = null;
/* 129 */     try { jks.load(trustStoreStream, password);
/* 130 */       clearArray(password);
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 128 */       localThrowable2 = localThrowable1;throw localThrowable1;
/*     */     }
/*     */     finally {
/* 131 */       if (trustStoreStream != null) if (localThrowable2 != null) try { trustStoreStream.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else trustStoreStream.close();
/*     */     }
/* 133 */     log.info("Trust store [{}] is in use for HTTPS", trustStoreFile);
/* 134 */     return SSLContexts.custom().useTLS().loadTrustMaterial(jks).build();
/*     */   }
/*     */   
/*     */ 
/*     */   static enum DefaultHttpClientContextFactory
/*     */     implements HttpClientContextFactory
/*     */   {
/* 141 */     INSTANCE;
/*     */     
/*     */     private DefaultHttpClientContextFactory() {}
/*     */     
/* 145 */     public HttpClientContext makeNew(HttpHost forTarget) { HttpClientContext hcc = HttpClientContext.create();
/* 146 */       hcc.setTargetHost(forTarget);
/* 147 */       return hcc;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/http/HttpSecurityModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */