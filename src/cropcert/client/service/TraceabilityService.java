package cropcert.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;

import cropcert.client.util.Utility;
import cropcert.traceability.ApiException;
import cropcert.traceability.api.LotApi;
import cropcert.traceability.api.LotCreationApi;
import cropcert.user.api.CollectionCenterApi;

public class TraceabilityService {
	
	@Inject
	private CollectionCenterApi collectionCenterApi;
	
	@Inject
	private LotCreationApi lotCreationApi;
	
	@Inject
	private LotApi lotApi;

	public Response getOrigins(Long lotId, String bearerToken) {
		if(lotId != -1 ) {
			List<Object> ccCodes;
			try {
				lotCreationApi.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, bearerToken);
				ccCodes = lotCreationApi.getLotOrigins(lotId.toString());
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
				collectionCenterApi.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, bearerToken);
				result = collectionCenterApi.getOriginNames(ccCodesString);
				return Response.ok().entity(result).build();
			} catch (cropcert.user.ApiException e) {
				e.printStackTrace();
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	public Response getShowPage(Long lotId, String bearerToken) {
		Map<String, Object> pageInfo = new HashMap<String, Object>();
		try {
			lotApi.find(lotId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().entity(pageInfo).build();
	}

}
