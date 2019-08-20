package cropcert.client.filter;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;

import cropcert.client.MyApplication;

public class JWTTokenValidationFilter implements Filter {

	private static JwtAuthenticator jwtAuthenticator;	
	private static String[] excludedUrls;
	
	@Override
	public void destroy() {
	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String path = ((HttpServletRequest) request).getServletPath();
		
		// exclude all the urls which starts with mention prefix in web.xml.
		if(Stream.of(excludedUrls)
				.anyMatch(path::startsWith)) {
			chain.doFilter(request, response);
			return;
		}
		
        String authorizationHeader = ((HttpServletRequest)request).getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        	((HttpServletResponse) response).setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
        	return;
        }

		CommonProfile commonProfile = getCommonProfile(authorizationHeader);
		if(commonProfile == null) {
			((HttpServletResponse) response).setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
			return;
		}
		
		chain.doFilter(request, response);
	}
	
	public static CommonProfile getCommonProfile(String authorizationHeader) {
		// Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
		return jwtAuthenticator.validateToken(token);
	}

	public CommonProfile isTokenValid(String token) {
		return jwtAuthenticator.validateToken(token);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		jwtAuthenticator = new JwtAuthenticator();
		jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration(MyApplication.JWT_SALT));
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			KeyPair rsaKeyPair = keyGen.generateKeyPair();
			jwtAuthenticator.addSignatureConfiguration(new RSASignatureConfiguration(rsaKeyPair));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		
		String excludePatterns = filterConfig.getInitParameter("excludedUrls");
		excludedUrls = excludePatterns.split(",");
		for(int i=0;i<excludedUrls.length;i++)
			excludedUrls[i] = excludedUrls[i].trim();
	}


}
