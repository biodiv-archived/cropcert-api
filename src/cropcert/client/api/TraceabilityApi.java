package cropcert.client.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.google.inject.Inject;

import cropcert.client.service.TraceabilityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Path("traceability")
@Api("Traceability")
@ApiImplicitParams({
    @ApiImplicitParam(name = "Authorization", value = "Authorization token", 
                      required = true, dataType = "string", paramType = "header") })
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
	public Response getOrigins(@Context HttpServletRequest request,
			@DefaultValue("-1") @QueryParam("lotId") Long lotId) throws JSONException {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		return traceabilityService.getOrigins(lotId, bearerToken);
	}
	
	@GET
	@Path("show")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get the information of the show page for lot",
			response = Map.class)
	public Response getShowPage(@Context HttpServletRequest request,
			@DefaultValue("-1") @QueryParam("lotId") Long lotId) throws JSONException {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		return traceabilityService.getShowPage(lotId, bearerToken);
	}
}
