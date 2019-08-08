package cropcert.client.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;

import com.google.inject.Inject;

import cropcert.client.service.TraceabilityService;
import cropcert.client.util.Utility;
import cropcert.traceability.ApiException;
import cropcert.traceability.api.LotCreationApi;
import cropcert.user.api.CollectionCenterApi;
import io.swagger.annotations.Api;

@Path("traceability")
@Api("Traceability")
public class TraceabilityApi {
	
	@Inject
	TraceabilityService traceabilityService;
	
	@Inject
	private CollectionCenterApi collectionCenterApi;
	
	@Inject
	private LotCreationApi LotCreationApi;
	
	@GET
	@Path("origin")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrigins(@DefaultValue("-1") @QueryParam("lotId") Long lotId) throws JSONException {
		
		if(lotId != -1 ) {
			List<Long> ccCodes;
			try {
				ccCodes = LotCreationApi.getLotOrigins(lotId.toString());
			} catch (ApiException e) {
				e.printStackTrace();
				return Response.status(Status.NO_CONTENT).build();
			}
			if(ccCodes == null || ccCodes.size() == 0) {
				return Response.status(Status.NOT_FOUND).entity("Lot id not found").build();
			}
			
			String ccCodesString = Utility.commaSeparatedValues(ccCodes);
			
			Map<String, Object> result;
			try {
				result = collectionCenterApi.getOriginNames(ccCodesString);
				return Response.ok().entity(result).build();
			} catch (cropcert.user.ApiException e) {
				e.printStackTrace();
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}
}
