package cropcert.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RedirectFilter implements Filter {

	private static final String USER_API         = "/user";
	private static final String TRACEABILITY_API = "/traceability";
	private static final String PAGES_API        = "/pages";
	
	@Override
	public void destroy() {
	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String path = ((HttpServletRequest) request).getServletPath();
		
        if (path.toLowerCase().startsWith(USER_API)) {
            request.getRequestDispatcher(path).forward(request, response);
        } 
        if (path.toLowerCase().startsWith(TRACEABILITY_API)) {
            request.getRequestDispatcher(path).forward(request, response);
        } 
        if (path.toLowerCase().startsWith(PAGES_API)) {
            request.getRequestDispatcher(path).forward(request, response);
        } 
        else {
            chain.doFilter(request, response);
        }
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
}
