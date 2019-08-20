package cropcert.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cropcert.client.util.Utility;
import io.swagger.jaxrs.config.BeanConfig;

public class MyApplication extends Application {

	public static final Logger logger = LoggerFactory.getLogger(MyApplication.class);

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
	}

	public MyApplication() {

		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");

		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion(properties.getProperty("version"));
		beanConfig.setTitle(properties.getProperty("title"));
		beanConfig.setSchemes(properties.getProperty("schemes").split(","));
		beanConfig.setHost(properties.getProperty("host"));
		beanConfig.setBasePath(properties.getProperty("basePath"));
		beanConfig.setResourcePackage(properties.getProperty("resourcePackage"));
		beanConfig.setPrettyPrint(new Boolean(properties.getProperty("prettyPrint")));
		beanConfig.setScan(new Boolean(properties.getProperty("scan")));

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
