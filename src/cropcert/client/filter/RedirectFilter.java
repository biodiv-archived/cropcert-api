package cropcert.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import cropcert.user.ApiClient;

public class RedirectFilter implements Filter {

	/**
	 * Add one variable for each microservice
	 */
	private static final String USER_API         = "/user/api/";
	private static final String TRACEABILITY_API = "/traceability/api/";
	private static final String PAGES_API        = "/pages/api/";
	
	@Inject
	private ApiClient userApiClient;
	
	@Inject
	private cropcert.traceability.ApiClient traceabilityApiClient;
	
	@Inject
	private cropcert.pages.ApiClient pagesApiClient;
	
	@Override
	public void destroy() {
	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String path     = ((HttpServletRequest) request).getServletPath();
		String basePath = "";
		
        if (path.toLowerCase().startsWith(USER_API)) {
        	path     = path.substring(USER_API.length()-1);
        	basePath = userApiClient.getBasePath();
        	((HttpServletResponse) response).sendRedirect(basePath+path);
        } 
        else if (path.toLowerCase().startsWith(TRACEABILITY_API)) {
        	path     = path.substring(TRACEABILITY_API.length()-1);
        	basePath = traceabilityApiClient.getBasePath();
        	((HttpServletResponse) response).sendRedirect(basePath+path);
        } 
        else if (path.toLowerCase().startsWith(PAGES_API)) {
        	path     = path.substring(PAGES_API.length()-1);
        	basePath = pagesApiClient.getBasePath();
        	((HttpServletResponse) response).sendRedirect(basePath+path);
        } 
        else {
            chain.doFilter(request, response);
        }
        
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
}
