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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * User end point for composite queries of the user end point.
 * @author vilay
 *
 */
@Path("user")
@Api("User")
@ApiImplicitParams({
    @ApiImplicitParam(name = "Authorization", value = "Authorization token", 
                      required = true, dataType = "string", paramType = "header") })
public class UserApi {
	
	@Path("me")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get details of the current user",
			response = CommonProfile.class)
	public CommonProfile getActor(@Context HttpServletRequest request) {
		return AuthUtility.getCurrentUser(request);
	}
}
