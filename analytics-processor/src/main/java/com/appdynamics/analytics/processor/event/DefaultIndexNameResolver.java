/*     */ package com.appdynamics.analytics.processor.event;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
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
/*     */ public class DefaultIndexNameResolver
/*     */   implements IndexNameResolver
/*     */ {
/*     */   static final String INDEX_SUFFIX_PATTERN = "yyyy-MM-dd_HH-mm-ss";
/*     */   public static final String FIELD_SEPARATOR = "___";
/*     */   public static final String SEARCH_SUFFIX = "search";
/*     */   static final String INSERT_SUFFIX = "insert";
/*     */   private static final String ALIAS_TEMPLATE = "%s___%s___%s";
/*  31 */   private static final Pattern SEARCH_ALIAS_PATTERN = Pattern.compile("(.*?)___(.*?)___search");
/*     */   
/*     */   public static final String ALL_SEARCH_ALIASES = "*___search";
/*     */   
/*     */   public static final String ALL_INSERT_ALIASES = "*___insert";
/*     */   
/*     */   public static final String ILLEGAL_INDEX_CHARACTERS_PATTERN = "[\\\\/*?\"<>| ,]";
/*     */   
/*     */   public static final String ILLEGAL_DOC_TYPE_CHARACTERS_PATTERN = "[.\\\\/*?\"<>| ,]";
/*     */   
/*     */   static final int INDEX_NAME_PARTS_LENGTH = 3;
/*     */   
/*     */   public static final String OLD_INDEX_MANAGEMENT_STATIC_INDEX_BASENAME = "active_static_index";
/*     */   public static final String OLD_INDEX_MANAGEMENT_DYNAMIC_INDEX_BASENAME = "active_dynamic_index";
/*     */   
/*     */   public String eventTypeFromIndex(String indexName)
/*     */   {
/*  48 */     Preconditions.checkArgument(indexName.contains("___"), "Type [" + indexName + "] is not a valid doc type name for the event" + " service");
/*     */     
/*     */ 
/*  51 */     return indexName.substring(indexName.indexOf("___") + "___".length());
/*     */   }
/*     */   
/*     */   public String accountNameFromIndex(String indexName)
/*     */   {
/*  56 */     Preconditions.checkArgument(indexName.contains("___"), "indexName [" + indexName + "] is not a valid " + "indexName or docType for the event service");
/*     */     
/*  58 */     return indexName.substring(0, indexName.indexOf("___"));
/*     */   }
/*     */   
/*     */   public String baseIndexNameFromFullName(String indexName)
/*     */   {
/*  63 */     Preconditions.checkArgument(indexName.contains("___"), "Expected index name [%s] to have a field separator '%s'", new Object[] { indexName, "___" });
/*     */     
/*  65 */     return indexName.substring(0, indexName.lastIndexOf("___"));
/*     */   }
/*     */   
/*     */   public DateTime indexCreationDateFromFullName(String indexName)
/*     */   {
/*  70 */     Preconditions.checkArgument(indexName.contains("___"), "Expected index name [%s] to have a field separator '%s'", new Object[] { indexName, "___" });
/*     */     
/*  72 */     String dateSuffix = indexName.substring(indexName.lastIndexOf("___") + "___".length());
/*  73 */     return DateTimeFormat.forPattern("yyyy-MM-dd_HH-mm-ss").withZone(DateTimeZone.UTC).parseDateTime(dateSuffix).withZone(DateTimeZone.UTC);
/*     */   }
/*     */   
/*     */ 
/*     */   public String appendTimestampToBaseIndexName(String baseIndexName)
/*     */   {
/*  79 */     return baseIndexName + "___" + DateTime.now().toString(DateTimeFormat.forPattern("yyyy-MM-dd_HH-mm-ss"));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSearchAlias(String alias)
/*     */   {
/*  85 */     return alias.endsWith("___search");
/*     */   }
/*     */   
/*     */   public boolean isInsertAlias(String alias)
/*     */   {
/*  90 */     return alias.endsWith("___insert");
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEventServiceIndex(String indexName)
/*     */   {
/*  96 */     String[] parts = indexName.split("___");
/*  97 */     if (parts.length != 3) {
/*  98 */       return false;
/*     */     }
/*     */     DateTime dt;
/*     */     try {
/* 102 */       dt = indexCreationDateFromFullName(indexName);
/*     */     } catch (Exception e) {
/* 104 */       return false;
/*     */     }
/* 106 */     return dt != null;
/*     */   }
/*     */   
/*     */   public boolean isOldIndexManagementIndex(String indexName)
/*     */   {
/* 111 */     return (isOldIndexManagementDynamicType(indexName)) || (isOldIndexManagementStaticType(indexName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String resolveOldIndexManagementDynamicIndexName()
/*     */   {
/* 118 */     return appendTimestampToBaseIndexName("active_dynamic_index");
/*     */   }
/*     */   
/*     */   public String resolveOldIndexManagementStaticIndexName()
/*     */   {
/* 123 */     return appendTimestampToBaseIndexName("active_static_index");
/*     */   }
/*     */   
/*     */   public String oldIndexManagementDynamicEventTypesInsertAlias()
/*     */   {
/* 128 */     return "active_dynamic_index___insert";
/*     */   }
/*     */   
/*     */   public String oldIndexManagementStaticEventTypesInsertAlias()
/*     */   {
/* 133 */     return "active_static_index___insert";
/*     */   }
/*     */   
/*     */   public String oldIndexManagementDynamicEventTypeDocType(String accountName, String eventType)
/*     */   {
/* 138 */     return AccountConfiguration.normalizeAccountName(accountName) + "___" + validateAndResolveEventType(eventType);
/*     */   }
/*     */   
/*     */   public String oldIndexManagementStaticEventTypeDocType(String accountName, String eventType)
/*     */   {
/* 143 */     return validateAndResolveEventType(eventType);
/*     */   }
/*     */   
/*     */   public boolean isOldIndexManagementDynamicType(String indexName)
/*     */   {
/* 148 */     return indexName.startsWith("active_dynamic_index___");
/*     */   }
/*     */   
/*     */   public boolean isOldIndexManagementStaticType(String indexName)
/*     */   {
/* 153 */     return indexName.startsWith("active_static_index___");
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
/*     */   public String resolveInsertAlias(String accountName, String eventType)
/*     */   {
/* 167 */     preconditions(accountName, eventType);
/* 168 */     return aliasWithSuffix(accountName, eventType, "insert");
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
/*     */   public String resolveSearchAlias(String accountName, String eventType)
/*     */   {
/* 182 */     preconditions(accountName, eventType);
/* 183 */     return aliasWithSuffix(accountName, eventType, "search");
/*     */   }
/*     */   
/*     */   public String accountNameFromSearchAlias(String searchAlias)
/*     */   {
/* 188 */     Matcher matcher = SEARCH_ALIAS_PATTERN.matcher(searchAlias);
/* 189 */     if (matcher.find()) {
/* 190 */       return matcher.group(1);
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */   
/*     */   public String eventTypeFromSearchAlias(String searchAlias)
/*     */   {
/* 197 */     Matcher matcher = SEARCH_ALIAS_PATTERN.matcher(searchAlias);
/* 198 */     if (matcher.find()) {
/* 199 */       return matcher.group(2);
/*     */     }
/* 201 */     return null;
/*     */   }
/*     */   
/*     */   public String resolveBaseIndexName(String accountName, String eventType)
/*     */   {
/* 206 */     return (AccountConfiguration.normalizeAccountName(accountName) + "___" + resolveEventType(eventType)).replaceAll("[\\\\/*?\"<>| ,]", "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getDocType(String accountName, String eventType)
/*     */   {
/* 213 */     return (AccountConfiguration.normalizeAccountName(accountName) + "___" + resolveEventType(eventType)).replaceAll("[.\\\\/*?\"<>| ,]", "");
/*     */   }
/*     */   
/*     */ 
/*     */   public String resolveEventType(String eventType)
/*     */   {
/* 219 */     return validateAndResolveEventType(eventType);
/*     */   }
/*     */   
/*     */   public static String validateAndResolveEventType(String eventType) {
/* 223 */     return ((String)Preconditions.checkNotNull(eventType, "Event Type cannot be null")).toLowerCase();
/*     */   }
/*     */   
/*     */   private String aliasWithSuffix(String accountName, String eventType, String suffix) {
/* 227 */     return String.format("%s___%s___%s", new Object[] { AccountConfiguration.normalizeAccountName(accountName), eventType, suffix }).toLowerCase();
/*     */   }
/*     */   
/*     */   private void preconditions(String accountName, String eventType)
/*     */   {
/* 232 */     if (Strings.isNullOrEmpty(accountName)) {
/* 233 */       throw new IllegalArgumentException("AccountName [" + accountName + "] must NOT be null or empty");
/*     */     }
/* 235 */     if (Strings.isNullOrEmpty(eventType)) {
/* 236 */       throw new IllegalArgumentException("EventType [" + eventType + "] must NOT be null or empty");
/*     */     }
/* 238 */     if (accountName.contains("___")) {
/* 239 */       throw new IllegalArgumentException("AccountName [" + accountName + "] cannot contain the sequence [" + "___" + "] - this sequence is used internally");
/*     */     }
/*     */     
/* 242 */     if (eventType.contains("___")) {
/* 243 */       throw new IllegalArgumentException("EventType [" + eventType + "] cannot contain the sequence [" + "___" + "] - this sequence is used internally");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/DefaultIndexNameResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */