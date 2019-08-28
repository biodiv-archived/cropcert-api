package cropcert.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;

import cropcert.client.util.Utility;
import cropcert.traceability.ApiException;
import cropcert.traceability.api.ActivityApi;
import cropcert.traceability.api.CuppingApi;
import cropcert.traceability.api.LotApi;
import cropcert.traceability.api.QualityReportApi;
import cropcert.traceability.model.Activity;
import cropcert.traceability.model.Lot;
import cropcert.user.api.CollectionCenterApi;
import cropcert.user.api.UserApi;
import cropcert.user.model.User;

public class TraceabilityService {
	
	@Inject private CollectionCenterApi collectionCenterApi;
	
	@Inject private LotApi lotApi;
	
	@Inject private ActivityApi activityApi;
	
	@Inject private UserApi userApi;
	
	@Inject private CuppingApi cuppingApi;
	
	@Inject private QualityReportApi qualityReportApi;
	
	public Response getOrigins(HttpServletRequest request, Long lotId) {
		if(lotId != -1 ) {
			List<Long> ccCodes;
			try {
				ccCodes = lotApi.getLotOrigins(lotId.toString());
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

	public Response getShowPage(HttpServletRequest request, Long lotId) {
		Map<String, Object> pageInfo = new HashMap<String, Object>();
		try {
			Lot lot = lotApi.find(lotId);
			pageInfo.put("lot", lot);
			List<Activity> activities = activityApi.getByLotId(lotId, -1, -1);
			pageInfo.put("activities", activities);
			Set<String> userIds = new HashSet<String>();
			for(Activity activity : activities) {
				userIds.add(activity.getUserId());
			}
			
			List<User> users = new ArrayList<User>();
			for(String id : userIds) {
				try {
					User user = userApi.find(Long.parseLong(id));
					users.add(user);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (cropcert.user.ApiException e) {
					e.printStackTrace();
				}
			}
			pageInfo.put("users", users);
			pageInfo.put("cupping_report", cuppingApi.getByLotId(lotId, null, null));
			pageInfo.put("quality_report", qualityReportApi.getByLotId(lotId, null, null));
			return Response.ok().entity(pageInfo).build();
		} catch (ApiException e) {
			e.printStackTrace();
			return Response.status(Status.NO_CONTENT).build();
		}
	}

}
