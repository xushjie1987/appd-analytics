/*    */package com.appdynamics.analytics.agent.field;

/*    */
/*    *//*    */import org.slf4j.Logger;
/*    */
import org.slf4j.LoggerFactory;

import com.appdynamics.analytics.agent.pipeline.dynamic.LogSourceJobFileParser;
import com.appdynamics.analytics.agent.pipeline.event.EventServiceClientModule;
/*    */
import com.appdynamics.analytics.agent.source.LogSources;
/*    */
import com.appdynamics.common.framework.util.Module;
/*    */
import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */
import com.google.inject.Inject;
/*    */
import com.google.inject.Provides;
/*    */
import com.google.inject.Singleton;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class ExtractedFieldsPollerModule
/*    */extends Module<ExtractedFieldsPollerConfiguration>
/*    */{
    /* 23 */private static final Logger log = LoggerFactory.getLogger(ExtractedFieldsPollerModule.class);
    
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
    /*    */
    /*    */@Provides
    /*    */@Singleton
    /*    */public ExtractedFieldsPoller provideExtractedFieldsPoller(LogSourceJobFileParser jobFileParser, LogSources logSources, ExtractedFieldsManager extractedFieldsManager, LifecycleInjector injector)
    /*    */{
        /* 39 */return injector.inject(new ExtractedFieldsPoller(jobFileParser, logSources, extractedFieldsManager, getConfiguration().getPollInterval().toMilliseconds(), getConfiguration().getInitialDelay().toMilliseconds()));
        /*    */}
    
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */@Provides
    /*    */@Singleton
    /*    */public ExtractedFieldsManager provideExtractedFieldsManager(EventServiceClientModule.EventServiceClientFactory factory)
    /*    */{
        /* 51 */return new ExtractedFieldsManager(factory.createExtractedFieldsClient(), factory.getAccountName(), factory.getAccessKey(), getConfiguration().getEventType());
        /*    */}
    
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */
    /*    */@Inject
    /*    */void onStart(ExtractedFieldsPoller extractedFieldsPoller)
    /*    */{
        /* 63 */log.debug("Started");
        /*    */}
    /*    */
}

/*
 * Location:
 * /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent
 * .jar!/com/appdynamics/analytics/agent/field/ExtractedFieldsPollerModule.class
 * Java compiler version: 7 (51.0) JD-Core Version: 0.7.1
 */
