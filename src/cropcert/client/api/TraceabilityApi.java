package cropcert.client.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.google.inject.Inject;

import cropcert.client.service.TraceabilityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("traceability")
@Api("Traceability")
public class TraceabilityApi {
	
	@Inject
	TraceabilityService traceabilityService;
	
	@GET
	@Path("origin")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get the origin by lot id",
			response = Map.class)
	public Response getOrigins(@DefaultValue("-1") @QueryParam("lotId") Long lotId) throws JSONException {
		return traceabilityService.getOrigins(lotId);
	}
	
	@GET
	@Path("show")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get the information of the show page for lot",
			response = Map.class)
	public Response getShowPage(@DefaultValue("-1") @QueryParam("lotId") Long lotId) throws JSONException {
		
		return traceabilityService.getShowPage(lotId);
	}
}
