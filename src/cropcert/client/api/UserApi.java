package cropcert.client.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.pac4j.core.profile.CommonProfile;

import cropcert.client.util.AuthUtility;
import io.swagger.annotations.Api;

/**
 * User end point for composite queries of the user end point.
 * @author vilay
 *
 */
@Path("api/user")
@Api("User")
public class UserApi {
	
	@Path("me")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CommonProfile getActor(@Context HttpServletRequest request) {
		return AuthUtility.getCurrentUser(request);
	}
}
