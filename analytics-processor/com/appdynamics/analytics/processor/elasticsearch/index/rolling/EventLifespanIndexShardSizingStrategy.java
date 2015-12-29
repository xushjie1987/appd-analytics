/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*    */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*    */ import com.appdynamics.analytics.processor.event.metadata.EventTypeMetaDataService;
/*    */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*    */ import org.elasticsearch.action.admin.indices.stats.IndexStats;
/*    */ import org.joda.time.DateTime;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class EventLifespanIndexShardSizingStrategy
/*    */   extends AbstractIndexShardSizingStrategy
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(EventLifespanIndexShardSizingStrategy.class);
/*    */   
/*    */   public static final int MAX_SHARDS_PER_INDEX = 25;
/*    */   
/*    */   private final TimeKeeper timeKeeper;
/*    */   
/*    */   private final IndexNameResolver indexNameResolver;
/*    */   
/*    */   private final EventTypeMetaDataService eventTypeMetaDataService;
/*    */   
/*    */   private final int defaultTargetIndexLifeInDays;
/*    */   
/*    */ 
/*    */   public EventLifespanIndexShardSizingStrategy(TimeKeeper timeKeeper, int defaultTargetIndexLifeInDays, IndexNameResolver indexNameResolver, EventTypeMetaDataService eventTypeMetaDataService)
/*    */   {
/* 40 */     this.timeKeeper = timeKeeper;
/* 41 */     this.indexNameResolver = indexNameResolver;
/* 42 */     this.defaultTargetIndexLifeInDays = defaultTargetIndexLifeInDays;
/* 43 */     this.eventTypeMetaDataService = eventTypeMetaDataService;
/*    */   }
/*    */   
/*    */   public int calculateNumberOfShards(IndexStats indexStats)
/*    */   {
/* 48 */     int numberOfPrimaries = getNumberOfPrimaryShards(indexStats);
/* 49 */     double daysBetween = calculateDaysBetweenNowAndIndexCreationDate(indexStats);
/* 50 */     double numberOfShardsForOneDay = numberOfPrimaries / daysBetween;
/* 51 */     int numberOfShardsForNewIndex = (int)Math.min(25.0D, Math.max(1.0D, Math.ceil(numberOfShardsForOneDay * getTargetIndexLifeInDays(indexStats.getIndex()))));
/*    */     
/* 53 */     log.debug("For index [{}], days between now and index creation is [{}], number of primary shards [{}], number of shards for new index [{}]", new Object[] { indexStats.getIndex(), Double.valueOf(daysBetween), Integer.valueOf(numberOfPrimaries), Integer.valueOf(numberOfShardsForNewIndex) });
/*    */     
/*    */ 
/*    */ 
/* 57 */     return numberOfShardsForNewIndex;
/*    */   }
/*    */   
/*    */   private int getTargetIndexLifeInDays(String index) {
/* 61 */     String accountName = this.indexNameResolver.accountNameFromIndex(index);
/* 62 */     String eventType = this.indexNameResolver.eventTypeFromIndex(index);
/* 63 */     EventTypeMetaData eventTypeMetaData = this.eventTypeMetaDataService.getEventTypeMetaData(accountName, eventType);
/* 64 */     if (eventTypeMetaData == null) {
/* 65 */       return this.defaultTargetIndexLifeInDays;
/*    */     }
/* 67 */     return (int)Math.ceil(eventTypeMetaData.getHotLifespanInDays().intValue() / 2.0D);
/*    */   }
/*    */   
/*    */   private double calculateDaysBetweenNowAndIndexCreationDate(IndexStats indexStats) {
/* 71 */     String indexName = indexStats.getIndex();
/* 72 */     DateTime indexCreationDate = this.indexNameResolver.indexCreationDateFromFullName(indexName);
/* 73 */     long millisBetween = this.timeKeeper.getCurrentMinute().getMillis() - indexCreationDate.getMillis();
/* 74 */     return millisBetween / 8.64E7D;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/EventLifespanIndexShardSizingStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */