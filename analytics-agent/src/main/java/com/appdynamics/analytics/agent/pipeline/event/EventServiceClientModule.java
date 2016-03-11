/*     */package com.appdynamics.analytics.agent.pipeline.event;

/*     */
/*     *//*     */import java.io.Closeable;
/*     */
import java.io.IOException;
/*     */
import java.net.URI;
/*     */
import java.util.List;
/*     */
import java.util.concurrent.CopyOnWriteArrayList;

/*     */
import javax.annotation.Nullable;
/*     */
import javax.annotation.PreDestroy;
/*     */
import javax.net.ssl.SSLContext;

/*     */
import org.apache.http.ParseException;
/*     */
import org.apache.http.config.Registry;
/*     */
import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */
import org.slf4j.Logger;
/*     */
import org.slf4j.LoggerFactory;

import com.appdynamics.analytics.shared.rest.client.eventservice.DefaultEventServiceClient;
/*     */
import com.appdynamics.analytics.shared.rest.client.eventservice.DefaultExtractedFieldsClient;
/*     */
import com.appdynamics.analytics.shared.rest.client.eventservice.EventServiceClient;
/*     */
import com.appdynamics.analytics.shared.rest.client.eventservice.ExtractedFieldsClient;
/*     */
import com.appdynamics.analytics.shared.rest.client.eventservice.creator.AnalyticsBizTxnEventTypeCreator;
/*     */
import com.appdynamics.analytics.shared.rest.client.eventservice.creator.AnalyticsLogTxnEventTypeCreator;
/*     */
import com.appdynamics.analytics.shared.rest.client.eventservice.creator.EventTypeCreator;
/*     */
import com.appdynamics.common.framework.util.Module;
/*     */
import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */
import com.appdynamics.common.util.health.SimpleHealthCheck;
/*     */
import com.google.common.base.Throwables;
/*     */
import com.google.inject.Inject;
/*     */
import com.google.inject.Provides;
/*     */
import com.google.inject.Singleton;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class EventServiceClientModule
/*     */extends Module<EventServiceConfiguration>
/*     */{
    /* 40 */private static final Logger log                       = LoggerFactory.getLogger(EventServiceClientModule.class);
    /*     */
    /*     */
    /*     */private static final int       MAX_CONNECTIONS_PER_ROUTE = 1;
    /*     */
    /*     */
    /*     */private static final int       MAX_TOTAL_CONNECTIONS     = 1;
    
    /*     */
    /*     */
    /*     */
    /*     */@Provides
    /*     */@Singleton
    /*     */public EventServiceClientFactory provideEventServiceClient(@Nullable SSLContext sslContext, Registry<ConnectionSocketFactory> socketFactoryRegistry)
    /*     */{
        /* 54 */return new EventServiceClientFactory(getConfiguration(), sslContext, socketFactoryRegistry);
        /*     */}
    
    /*     */
    /*     */
    /*     */
    /*     */public static class EventServiceClientFactory
    /*     */{
        /*     */private final URI                               baseUri;
        /*     */
        /*     */
        /*     */private final SSLContext                        sslContext;
        /*     */
        /*     */private final Registry<ConnectionSocketFactory> socketFactoryRegistry;
        /*     */
        /*     */private final String                            accountName;
        /*     */
        /*     */private final String                            accessKey;
        /*     */
        /*     */final List<Closeable>                           closeableClients;
        /*     */
        /*     */private final EventServiceConfiguration         configuration;
        
        /*     */
        /*     */
        /*     */EventServiceClientFactory(EventServiceConfiguration configuration, @Nullable SSLContext sslContext, Registry<ConnectionSocketFactory> socketFactoryRegistry)
        /*     */{
            /* 79 */this.baseUri = URI.create(configuration.getEndpoint());
            /* 80 */this.sslContext = sslContext;
            /* 81 */this.socketFactoryRegistry = socketFactoryRegistry;
            /* 82 */this.accountName = configuration.getAccountName();
            /* 83 */this.accessKey = configuration.getAccessKey();
            /* 84 */this.closeableClients = new CopyOnWriteArrayList();
            /* 85 */this.configuration = configuration;
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */public EventServiceClient createEventServiceClient()
        /*     */{
            /* 94 */EventServiceClientModule.log.info("Created EventServiceClient for account [{}]", this.accountName);
            /* 95 */DefaultEventServiceClient client = createDefaultEventServiceClient();
            /* 96 */this.closeableClients.add(client);
            /* 97 */return client;
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */public ExtractedFieldsClient createExtractedFieldsClient()
        /*     */{
            /* 106 */EventServiceClientModule.log.info("Created ExtractedFieldsClient for account [{}]", this.accountName);
            /* 107 */DefaultExtractedFieldsClient client = createDefaultExtractedFieldsClient();
            /* 108 */this.closeableClients.add(client);
            /* 109 */return client;
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */DefaultEventServiceClient createDefaultEventServiceClient()
        /*     */{
            /* 117 */DefaultEventServiceClient client = DefaultEventServiceClient.builder(this.baseUri.getScheme(), this.baseUri.getHost(), this.baseUri.getPort()).socketTimeoutMillis(this.configuration.getSocketTimeoutMillis()).connectionTimeoutMillis(this.configuration.getConnectionTimeoutMillis()).sslContext(this.sslContext).socketFactoryRegistry(this.socketFactoryRegistry).maxConnectionsPerRoute(1).maxTotalConnections(1).proxyConfig(this.configuration.proxyHost, this.configuration.proxyPort, this.configuration.proxyUsername, this.configuration.proxyPassword).build();
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
            /* 128 */client.registerEventTypeCreator(new EventTypeCreator[] { new AnalyticsBizTxnEventTypeCreator(), new AnalyticsLogTxnEventTypeCreator() });
            /*     */
            /* 130 */return client;
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */DefaultExtractedFieldsClient createDefaultExtractedFieldsClient()
        /*     */{
            /* 138 */return DefaultExtractedFieldsClient.builder(this.baseUri.getScheme(), this.baseUri.getHost(), this.baseUri.getPort()).socketTimeoutMillis(this.configuration.getSocketTimeoutMillis()).connectionTimeoutMillis(this.configuration.getConnectionTimeoutMillis()).sslContext(this.sslContext).socketFactoryRegistry(this.socketFactoryRegistry).maxConnectionsPerRoute(1).maxTotalConnections(1).proxyConfig(this.configuration.proxyHost, this.configuration.proxyPort, this.configuration.proxyUsername, this.configuration.proxyPassword).build();
            /*     */}
        
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
        /*     */public String getAccountName()
        /*     */{
            /* 155 */return this.accountName;
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */public String getAccessKey()
        /*     */{
            /* 162 */return this.accessKey;
            /*     */}
        
        /*     */
        /*     */
        /*     */
        /*     */@PreDestroy
        /*     */void stop()
        /*     */{
            /* 170 */for (Closeable client : this.closeableClients) {
                /*     */try {
                    /* 172 */client.close();
                    /*     */} catch (IOException e) {
                    /* 174 */EventServiceClientModule.log.error("Could not close http client connection", e);
                    /*     */}
                /*     */}
            /* 177 */this.closeableClients.clear();
            /*     */}
        /*     */
    }
    
    /*     */
    /*     */@Inject
    /*     */void start(EventServiceClientFactory eventServiceClientFactory, ConsolidatedHealthCheck healthCheck) {
        /*     */try {
            /* 184 */eventServiceClientFactory.createEventServiceClient().ping(eventServiceClientFactory.getAccountName(), eventServiceClientFactory.getAccessKey());
            /*     */}
        /*     */catch (ParseException e) {
            /* 187 */log.error("The Event Service could not be contacted. Please verify that the account name, access key and the endpoint address were specified correctly", e);
            /*     */
            /* 189 */throw e;
            /*     */} catch (Exception e) {
            /* 191 */String msg = "The Event Service could not be contacted. Please verify that the account name, access key and the endpoint address were specified correctly. It could also be the case that the service is not running yet";
            /*     */
            /*     */
            /* 194 */if (log.isDebugEnabled())
            /*     */{
                /* 196 */log.warn(msg, e);
                /*     */}
            /*     */else {
                /* 199 */String s = Throwables.getRootCause(e).getMessage();
                /* 200 */log.warn(msg + ". Actual error message [" + s + "]");
                /*     */}
            /*     */}
        /*     */
        /* 204 */healthCheck.register(new EventServiceConnectivityHealthCheck(eventServiceClientFactory));
        /*     */}
    
    /*     */
    /*     */static class EventServiceConnectivityHealthCheck extends SimpleHealthCheck {
        /*     */final EventServiceClient client;
        /*     */final String             accountName;
        /*     */final String             accessKey;
        
        /*     */
        /*     */public EventServiceConnectivityHealthCheck(EventServiceClientModule.EventServiceClientFactory esf) {
            /* 213 */super();
            /* 214 */this.client = esf.createEventServiceClient();
            /* 215 */this.accountName = esf.getAccountName();
            /* 216 */this.accessKey = esf.getAccessKey();
            /*     */}
        
        /*     */
        /*     */@Override
        public Result check()
        /*     */{
            /* 221 */SimpleHealthCheck.callAndCheck(new Runnable()
            /*     */{
                /*     */@Override
                public void run()
                /*     */{
                    /* 225 */EventServiceClientModule.EventServiceConnectivityHealthCheck.this.client.ping(EventServiceClientModule.EventServiceConnectivityHealthCheck.this.accountName, EventServiceClientModule.EventServiceConnectivityHealthCheck.this.accessKey);
                    /*     */}
                /*     */
            });
            /*     */}
        /*     */
    }
    /*     */
}

/*
 * Location:
 * /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent
 * .jar!/com
 * /appdynamics/analytics/agent/pipeline/event/EventServiceClientModule.class
 * Java compiler version: 7 (51.0) JD-Core Version: 0.7.1
 */
