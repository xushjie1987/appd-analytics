/*    */ package com.appdynamics.analytics.processor.rest;
/*    */ 
/*    */ import javax.ws.rs.core.Response.Status;
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
/*    */ public enum StandardErrorCode
/*    */   implements RestErrorCode
/*    */ {
/* 18 */   CODE_ACCOUNT_NOT_FOUND(Response.Status.NOT_FOUND, "Account.NotFound"), 
/* 19 */   CODE_ALREADY_EXISTS_EXTRACTED_FIELD(Response.Status.CONFLICT, "ExtractedField.AlreadyExists"), 
/* 20 */   CODE_AUTH_EXPIRED(Response.Status.UNAUTHORIZED, "Auth.Expired"), 
/* 21 */   CODE_AUTH_UNAUTHORIZED(Response.Status.UNAUTHORIZED, "Auth.Unauthorized"), 
/* 22 */   CODE_BULK_ERROR(Response.Status.BAD_REQUEST, "BulkError.BadRequest"), 
/* 23 */   CODE_CONFLICT_CLUSTER_NAME(Response.Status.CONFLICT, "Conflict.ClusterName"), 
/* 24 */   CODE_CONFLICT_EVENT_TYPE(Response.Status.CONFLICT, "Conflict.EventType"), 
/* 25 */   CODE_CONFLICT_ROUTE(Response.Status.CONFLICT, "Conflict.Route"), 
/* 26 */   CODE_EVENT_TYPE_BYTES_LIMIT_REACHED(Response.Status.NOT_ACCEPTABLE, "BytesLimitReached.EventType"), 
/* 27 */   CODE_EVENT_TYPE_DOC_LIMIT_REACHED(Response.Status.NOT_ACCEPTABLE, "DocumentLimitReached.EventType"), 
/* 28 */   CODE_EVENT_TYPE_EXPIRED(Response.Status.NOT_ACCEPTABLE, "Expired.EventType"), 
/* 29 */   CODE_EVENT_TYPE_SEARCH_LIMIT_REACHED(Response.Status.NOT_ACCEPTABLE, "SearchLimitReached.EventType"), 
/* 30 */   CODE_HIDDEN_FIELD_NOT_EXISTS(Response.Status.BAD_REQUEST, "Missing.HiddenField"), 
/* 31 */   CODE_INTERNAL_TRANSIENT_FAILURE(Response.Status.INTERNAL_SERVER_ERROR, "Internal.Transient"), 
/* 32 */   CODE_INVALID_ACCOUNT_NAME(Response.Status.BAD_REQUEST, "Invalid.AccountName"), 
/* 33 */   CODE_INVALID_BYTE_SIZE(Response.Status.BAD_REQUEST, "Invalid.ByteSize"), 
/* 34 */   CODE_INVALID_DATE_TIME(Response.Status.BAD_REQUEST, "Invalid.DateTime"), 
/* 35 */   CODE_INVALID_EXTRACTED_FIELD_NAME(Response.Status.BAD_REQUEST, "Invalid.ExtractedFieldName"), 
/* 36 */   CODE_INVALID_EVENT_TYPE(Response.Status.NOT_FOUND, "Invalid.EventType"), 
/* 37 */   CODE_INVALID_REGEX_PATTERN(Response.Status.BAD_REQUEST, "Invalid.RegexPattern"), 
/* 38 */   CODE_INVALID_REQUEST_BODY(Response.Status.BAD_REQUEST, "Invalid.RequestBody"), 
/* 39 */   CODE_INVALID_SEARCH_REQUEST(Response.Status.BAD_REQUEST, "Invalid.SearchRequest"), 
/* 40 */   CODE_INVALID_ONE_CAPTURING_GROUP_NAME_REQUIRED(Response.Status.BAD_REQUEST, "Invalid.OneCapturingGroupRequired"), 
/*    */   
/* 42 */   CODE_MISSING_ACCOUNT_NAMES(Response.Status.BAD_REQUEST, "Missing.AccountNames"), 
/* 43 */   CODE_MISSING_BODY(Response.Status.BAD_REQUEST, "MissingBody"), 
/* 44 */   CODE_MISSING_CAPTURING_GROUP_NAME_EXTRACTED_FIELD(Response.Status.BAD_REQUEST, "Missing.CapturingGroupNameExtractedField"), 
/*    */   
/* 46 */   CODE_MISSING_CLUSTER_NAMES(Response.Status.BAD_REQUEST, "Missing.ClusterNames"), 
/* 47 */   CODE_MISSING_EXTRACTED_FIELD(Response.Status.NOT_FOUND, "Missing.ExtractedField"), 
/* 48 */   CODE_MISSING_EVENT_TYPE(Response.Status.NOT_FOUND, "Missing.EventType"), 
/* 49 */   CODE_MISSING_ID_PATH(Response.Status.BAD_REQUEST, "Missing.idPath"), 
/* 50 */   CODE_MISSING_LICENSE_DATA(Response.Status.BAD_REQUEST, "Missing.LicenseData"), 
/* 51 */   CODE_NOT_FOUND_CLUSTER_NAME(Response.Status.NOT_FOUND, "NotFound.ClusterName"), 
/* 52 */   CODE_RESOURCE_EXCLUDED(Response.Status.SERVICE_UNAVAILABLE, "Retry.Resource"), 
/* 53 */   CODE_RETRY_EVENT_TYPE(Response.Status.NOT_FOUND, "Retry.EventType"), 
/* 54 */   CODE_ROLE_UNAUTHORIZED(Response.Status.UNAUTHORIZED, "Role.Unauthorized"), 
/* 55 */   CODE_ROUTE_NOT_FOUND(Response.Status.NOT_FOUND, "NotFound.Route"), 
/* 56 */   CODE_SERVICE_UNAVAILABLE(Response.Status.SERVICE_UNAVAILABLE, "Service.Unavailable"), 
/* 57 */   CODE_UNKNOWN_FAILURE(Response.Status.INTERNAL_SERVER_ERROR, "Unknown"), 
/* 58 */   CODE_UNKNOWN_SEARCH_FAILURE(Response.Status.INTERNAL_SERVER_ERROR, "Unknown.SearchFailure"), 
/* 59 */   CODE_UNPARSEABLE_REQUEST(Response.Status.BAD_REQUEST, "UnparseableRequest");
/*    */   
/*    */   private final Response.Status status;
/*    */   private final String subStatus;
/*    */   
/*    */   private StandardErrorCode(Response.Status status, String subStatus) {
/* 65 */     this.status = status;
/* 66 */     this.subStatus = subStatus;
/*    */   }
/*    */   
/*    */   public Response.Status getStatus()
/*    */   {
/* 71 */     return this.status;
/*    */   }
/*    */   
/*    */   public String getSubStatus()
/*    */   {
/* 76 */     return this.subStatus;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/StandardErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */