/*     */ package com.appdynamics.analytics.shared.rest.client.utils;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.io.BaseEncoding;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.config.RequestConfig.Builder;
/*     */ import org.apache.http.client.utils.URIBuilder;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.config.SocketConfig.Builder;
/*     */ import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLContextBuilder;
/*     */ import org.apache.http.conn.ssl.SSLContexts;
/*     */ import org.apache.http.impl.client.BasicCredentialsProvider;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAnalyticsClient
/*     */   implements Closeable
/*     */ {
/*     */   private final CloseableHttpClient client;
/*     */   private final GenericCrudClient crudClient;
/*     */   private final HttpRequestFactory requestFactory;
/*     */   private final ObjectMapper mapper;
/*     */   
/*     */   protected CloseableHttpClient getClient()
/*     */   {
/*  57 */     return this.client;
/*     */   }
/*     */   
/*  60 */   protected GenericCrudClient getCrudClient() { return this.crudClient; }
/*     */   
/*     */   protected HttpRequestFactory getRequestFactory() {
/*  63 */     return this.requestFactory;
/*     */   }
/*     */   
/*  66 */   protected ObjectMapper getMapper() { return this.mapper; }
/*     */   
/*     */   protected AbstractAnalyticsClient(ObjectMapper mapper, CloseableHttpClient client, URI baseUri)
/*     */   {
/*  70 */     this.mapper = mapper;
/*  71 */     this.client = client;
/*  72 */     this.requestFactory = new HttpRequestFactory(mapper, client, baseUri);
/*  73 */     this.crudClient = new GenericCrudClient(this.requestFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  81 */     this.client.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String buildStandardAuthHeader(String accountName, String accessKey)
/*     */   {
/*  91 */     return "Basic " + BaseEncoding.base64().encode(new StringBuilder().append(accountName).append(":").append(accessKey).toString().getBytes(Charsets.UTF_8));
/*     */   }
/*     */   
/*     */   public static abstract class Builder<T> {
/*  95 */     private static final Logger log = LoggerFactory.getLogger(Builder.class);
/*     */     
/*     */     static final String PROTOCOL_HTTP = "http";
/*     */     
/*     */     static final String PROTOCOL_HTTPS = "https";
/*     */     
/*     */     static final String TRUST_STORE_TYPE = "JKS";
/*     */     
/*     */     protected final String hostName;
/*     */     
/*     */     protected final int port;
/*     */     
/*     */     protected final String scheme;
/*     */     protected ObjectMapper mapper;
/*     */     protected CloseableHttpClient client;
/* 110 */     protected int maxTotalConnections = 200;
/* 111 */     protected int maxConnectionsPerRoute = 20;
/* 112 */     protected int socketTimeoutMillis = 30000;
/* 113 */     protected int connectionTimeoutMillis = 5000;
/* 114 */     protected SSLContext sslContext = null;
/*     */     protected Registry<ConnectionSocketFactory> socketFactoryRegistry;
/* 116 */     protected String trustStorePasswordBase64 = null;
/* 117 */     protected String trustStoreFile = null;
/* 118 */     protected String httpProxyHost = null;
/* 119 */     protected Integer httpProxyPort = null;
/* 120 */     protected String httpProxyUsername = null;
/* 121 */     protected String httpProxyPassword = null;
/*     */     
/*     */     protected Builder(String scheme, String hostName, int port) {
/* 124 */       this.scheme = scheme;
/* 125 */       this.hostName = hostName;
/* 126 */       this.port = port;
/*     */     }
/*     */     
/*     */     protected Builder(String hostName, int port) {
/* 130 */       this.scheme = "http";
/* 131 */       this.hostName = hostName;
/* 132 */       this.port = port;
/*     */     }
/*     */     
/*     */     public Builder<T> maxTotalConnections(int maxTotalConnections) {
/* 136 */       this.maxTotalConnections = maxTotalConnections;
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> maxConnectionsPerRoute(int maxConnectionsPerRoute) {
/* 141 */       this.maxConnectionsPerRoute = maxConnectionsPerRoute;
/* 142 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> socketTimeoutMillis(int socketTimeoutMillis) {
/* 146 */       this.socketTimeoutMillis = socketTimeoutMillis;
/* 147 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> connectionTimeoutMillis(int connectionTimeoutMillis) {
/* 151 */       this.connectionTimeoutMillis = connectionTimeoutMillis;
/* 152 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> sslContext(SSLContext sslContext) {
/* 156 */       this.sslContext = sslContext;
/* 157 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> trustStoreConfig(String trustStoreFile, String trustStorePasswordBase64) {
/* 161 */       this.trustStorePasswordBase64 = trustStorePasswordBase64;
/* 162 */       this.trustStoreFile = trustStoreFile;
/* 163 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> proxyConfig(String httpProxyHost, Integer httpProxyPort, String httpProxyUsername, String httpProxyPassword)
/*     */     {
/* 168 */       this.httpProxyHost = httpProxyHost;
/* 169 */       this.httpProxyPort = httpProxyPort;
/* 170 */       this.httpProxyUsername = httpProxyUsername;
/* 171 */       this.httpProxyPassword = httpProxyPassword;
/* 172 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> socketFactoryRegistry(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
/* 176 */       this.socketFactoryRegistry = socketFactoryRegistry;
/* 177 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> mapper(ObjectMapper mapper) {
/* 181 */       this.mapper = mapper;
/* 182 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> client(CloseableHttpClient client) {
/* 186 */       this.client = client;
/* 187 */       return this;
/*     */     }
/*     */     
/*     */     public T build() {
/* 191 */       if (this.mapper == null) {
/* 192 */         this.mapper = new ObjectMapper();
/*     */       }
/*     */       
/* 195 */       if (this.client == null) {
/* 196 */         this.client = createClient();
/*     */       }
/* 198 */       return (T)buildInternal();
/*     */     }
/*     */     
/*     */     private CloseableHttpClient createClient() {
/* 202 */       PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(getSocketFactoryRegistry());
/*     */       
/* 204 */       connectionManager.setMaxTotal(this.maxTotalConnections);
/* 205 */       connectionManager.setDefaultMaxPerRoute(this.maxConnectionsPerRoute);
/*     */       
/* 207 */       HttpClientBuilder builder = HttpClients.custom();
/* 208 */       builder.setSslcontext(this.sslContext).setDefaultSocketConfig(SocketConfig.custom().setSoReuseAddress(true).build()).setDefaultRequestConfig(getRequestConfig()).setConnectionManager(connectionManager);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 213 */       addProxyConfig(builder);
/* 214 */       return builder.build();
/*     */     }
/*     */     
/*     */     private void addProxyConfig(HttpClientBuilder builder) {
/* 218 */       if ((!Strings.isNullOrEmpty(this.httpProxyHost)) && (this.httpProxyPort != null)) {
/* 219 */         HttpHost proxy = new HttpHost(this.httpProxyHost, this.httpProxyPort.intValue());
/* 220 */         builder.setProxy(proxy);
/* 221 */         if ((!Strings.isNullOrEmpty(this.httpProxyUsername)) && (!Strings.isNullOrEmpty(this.httpProxyPassword))) {
/* 222 */           CredentialsProvider credsProvider = new BasicCredentialsProvider();
/* 223 */           Credentials credentials = new UsernamePasswordCredentials(this.httpProxyUsername, this.httpProxyPassword);
/* 224 */           credsProvider.setCredentials(new AuthScope(this.httpProxyHost, this.httpProxyPort.intValue()), credentials);
/* 225 */           builder.setDefaultCredentialsProvider(credsProvider);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected RequestConfig getRequestConfig() {
/* 231 */       return RequestConfig.custom().setSocketTimeout(this.socketTimeoutMillis).setConnectTimeout(this.connectionTimeoutMillis).build();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private Registry<ConnectionSocketFactory> getSocketFactoryRegistry()
/*     */     {
/* 238 */       if (this.socketFactoryRegistry == null) {
/* 239 */         RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory());
/*     */         
/*     */ 
/*     */ 
/* 243 */         configureHttps(builder);
/*     */         
/* 245 */         this.socketFactoryRegistry = builder.build();
/*     */       }
/* 247 */       return this.socketFactoryRegistry;
/*     */     }
/*     */     
/*     */     private void configureHttps(RegistryBuilder<ConnectionSocketFactory> builder) {
/* 251 */       if (this.sslContext == null) {
/*     */         try {
/* 253 */           this.sslContext = buildSslContextFromTrustStoreOrDefault();
/*     */         } catch (Exception e) {
/* 255 */           throw Throwables.propagate(e);
/*     */         }
/*     */       }
/*     */       
/* 259 */       builder.register("https", new SSLConnectionSocketFactory(this.sslContext));
/*     */     }
/*     */     
/*     */     private static char[] base64ToClearAscii(String optBase64String) {
/* 263 */       if ((optBase64String == null) || (optBase64String.length() == 0)) {
/* 264 */         return null;
/*     */       }
/*     */       
/* 267 */       byte[] clearTxtPassword = BaseEncoding.base64().decode(optBase64String);
/* 268 */       return new String(clearTxtPassword, StandardCharsets.US_ASCII).toCharArray();
/*     */     }
/*     */     
/*     */     private static void clearArray(char[] chars) {
/* 272 */       if (chars != null)
/*     */       {
/* 274 */         for (int i = 0; i < chars.length; i++) {
/* 275 */           chars[i] = '\000';
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private SSLContext buildSslContextFromTrustStoreOrDefault() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException
/*     */     {
/* 282 */       if ((Strings.isNullOrEmpty(this.trustStoreFile)) || (Strings.isNullOrEmpty(this.trustStorePasswordBase64))) {
/* 283 */         return SSLContext.getDefault();
/*     */       }
/*     */       
/* 286 */       KeyStore jks = KeyStore.getInstance("JKS");
/*     */       
/* 288 */       char[] password = null;
/* 289 */       if (this.trustStorePasswordBase64 != null) {
/* 290 */         password = base64ToClearAscii(this.trustStorePasswordBase64);
/*     */       }
/*     */       
/* 293 */       InputStream trustStoreStream = new FileInputStream(this.trustStoreFile);Throwable localThrowable2 = null;
/* 294 */       try { jks.load(trustStoreStream, password);
/* 295 */         clearArray(password);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 293 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally {
/* 296 */         if (trustStoreStream != null) if (localThrowable2 != null) try { trustStoreStream.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else trustStoreStream.close();
/*     */       }
/* 298 */       log.info("Trust store [{}] is in use for HTTPS", this.trustStoreFile);
/* 299 */       return SSLContexts.custom().useTLS().loadTrustMaterial(jks).build();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected URI buildBaseUri(String path)
/*     */     {
/*     */       try
/*     */       {
/* 308 */         URIBuilder builder = new URIBuilder();
/* 309 */         builder.setScheme(this.scheme).setHost(this.hostName).setPath(path);
/* 310 */         if ((this.port != 80) && (this.port != 443)) {
/* 311 */           builder.setPort(this.port);
/*     */         }
/* 313 */         return builder.build();
/*     */       } catch (URISyntaxException e) {
/* 315 */         throw Throwables.propagate(e);
/*     */       }
/*     */     }
/*     */     
/*     */     protected abstract T buildInternal();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/AbstractAnalyticsClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */