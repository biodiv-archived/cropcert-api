package cropcert.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cropcert.client.util.Utility;
import io.swagger.jaxrs.config.BeanConfig;

public class MyApplication extends Application{
	
	public static final Logger logger = LoggerFactory.getLogger(MyApplication.class);
	
	public MyApplication() {
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0");
		beanConfig.setTitle("Cropcert api module microServices");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/cropcert/api");
		beanConfig.setResourcePackage("cropcert.client");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			List<Class<?>> apiClasses = Utility.getApiAnnotatedClassesFromPackage("cropcert");
			classes.addAll(apiClasses);
		} catch (ClassNotFoundException | IOException | URISyntaxException e) {
			logger.error(e.getMessage());
		}
		
		classes.add(io.swagger.jaxrs.listing.ApiListingResource.class);
		classes.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		return classes;
	}
}
