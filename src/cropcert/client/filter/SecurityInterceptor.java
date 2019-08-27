package cropcert.client.filter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;

import net.minidev.json.JSONArray;

/**
 * This is the security filter. This can be applied to any method it want.
 * Method should have annotation named {@code TokenAndUserAuthenticated} This
 * annotation take user allowed to execute as input. If everything is fine then
 * it passes the security
 * 
 * @author vilay
 *
 */
public class SecurityInterceptor implements MethodInterceptor {

	public static JwtAuthenticator jwtAuthenticator;
	public static final String JWT_SALT;

	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JWT_SALT = properties.getProperty("jwtSalt", "12345678901234567890123456789012");

		jwtAuthenticator = new JwtAuthenticator();
		jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration(JWT_SALT));
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			KeyPair rsaKeyPair = keyGen.generateKeyPair();
			jwtAuthenticator.addSignatureConfiguration(new RSASignatureConfiguration(rsaKeyPair));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Method method = invocation.getMethod();

		if (!method.isAnnotationPresent(TokenAndUserAuthenticated.class))
			return invocation.proceed();

		// Get the parameter index of the HttpServletRequest
		int parameterIndex = getRequestParameterIndex(method);
		if (parameterIndex == -1)
			return Response.status(Status.UNAUTHORIZED).entity("Api end-point should have request as parameter")
					.build();

		// Extract the request out of method using parameter index.
		HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[parameterIndex];
		String authorizationHeader = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return Response.status(Status.UNAUTHORIZED).entity("Missing autherization header in request").build();
		}

		// Extract token from the authorization header.
		String token = authorizationHeader.substring("Bearer".length()).trim();

		// Check if the token is valid.
		CommonProfile commonProfile = jwtAuthenticator.validateToken(token);
		if (commonProfile == null) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid JWT token").build();
		}

		// Get roles allowed from the annotation
		TokenAndUserAuthenticated annotation = method.getAnnotation(TokenAndUserAuthenticated.class);
		Set<String> allowedPermissions = new HashSet<String>(Arrays.asList(annotation.permissions()));

		JSONArray userPermissions = (JSONArray) commonProfile.getAttribute("roles");
		
		// Special grand for admin to do everthing
		if(userPermissions.contains(Permissions.ADMIN))
			return invocation.proceed();

		userPermissions.retainAll(allowedPermissions);
		
		if(userPermissions.size() <= 0) {
			return Response.status(Status.UNAUTHORIZED).entity("User is not autherized to perform this action").build();
		}

		// If everthing is fine proceed with the method invocation.
		return invocation.proceed();
	}

	private int getRequestParameterIndex(Method method) {
		int i = 0;
		for (Parameter parameter : method.getParameters()) {
			if (parameter.isAnnotationPresent(Context.class) && parameter.getType().equals(HttpServletRequest.class))
				return i;
			i++;
		}

		return -1;
	}
}
