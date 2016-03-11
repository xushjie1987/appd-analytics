package com.appdynamics.analytics.processor.event.resource;

import com.sun.jersey.api.core.HttpContext;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract interface RestfulExtractedFields
{
  @GET
  public abstract Response getExtractedFields(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString, @QueryParam("source-types") List<String> paramList);
  
  @GET
  public abstract Response getExtractedField(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @PathParam("extractedFieldName") String paramString2);
  
  @POST
  public abstract Response validateExtractedField(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @PathParam("extractedFieldName") String paramString2, String paramString3);
  
  @POST
  public abstract Response createExtractedField(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @PathParam("extractedFieldName") String paramString2, String paramString3);
  
  @PUT
  public abstract Response updateExtractedField(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @PathParam("extractedFieldName") String paramString2, String paramString3);
  
  @DELETE
  public abstract Response deleteExtractedField(@Context HttpContext paramHttpContext, @PathParam("eventType") String paramString1, @PathParam("extractedFieldName") String paramString2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/RestfulExtractedFields.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */