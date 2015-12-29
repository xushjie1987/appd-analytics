package com.appdynamics.analytics.processor.event.resource;

import com.appdynamics.analytics.processor.event.hiddenfields.HiddenField;
import com.sun.jersey.api.core.HttpContext;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract interface RestfulEventService
{
  @POST
  public abstract Response registerEventType(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, String paramString2);
  
  @PUT
  public abstract Response updateEventType(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, String paramString2);
  
  @PUT
  public abstract Response bulkUpdateEventType(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, String paramString2);
  
  @GET
  public abstract Response getEventType(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString);
  
  @GET
  public abstract Response getEventTypeDocumentsUsage(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @QueryParam("startDateTime") String paramString2, @QueryParam("endDateTime") String paramString3);
  
  @GET
  public abstract Response getEventTypeDocFragmentsUsage(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @QueryParam("date") String paramString2, @QueryParam("hourOfDay") int paramInt);
  
  @GET
  public abstract Response getEventTypeBytesUsage(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @QueryParam("date") String paramString2, @QueryParam("hourOfDay") int paramInt);
  
  @DELETE
  public abstract Response deleteEventType(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString);
  
  @POST
  public abstract Response publishEvents(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString, byte[] paramArrayOfByte);
  
  @POST
  public abstract Response moveEvents(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString, MoveEventTypeRequest paramMoveEventTypeRequest);
  
  @PATCH
  public abstract Response upsertEvents(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, @QueryParam("_idPath") String paramString2, @QueryParam("_mergeFields") String paramString3, byte[] paramArrayOfByte);
  
  @POST
  public abstract Response searchEvents(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, String paramString2);
  
  @POST
  public abstract Response multiSearchEvents(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, String paramString);
  
  @POST
  public abstract Response queryEvents(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt1, @QueryParam("start") @DefaultValue("") String paramString1, @QueryParam("end") @DefaultValue("") String paramString2, @QueryParam("limit") @DefaultValue("100") int paramInt2, @QueryParam("returnEsJson") @DefaultValue("false") boolean paramBoolean, String paramString3);
  
  @POST
  public abstract Response hideFields(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, String paramString2);
  
  @DELETE
  public abstract Response showHiddenField(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, @PathParam("fieldName") String paramString2);
  
  @GET
  public abstract List<HiddenField> listHiddenFields(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString);
  
  @GET
  public abstract Response ping(@Context HttpContext paramHttpContext);
  
  @POST
  public abstract Response relevantFields(@Context HttpContext paramHttpContext, @PathParam("version") int paramInt, @PathParam("eventType") String paramString1, String paramString2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/RestfulEventService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */