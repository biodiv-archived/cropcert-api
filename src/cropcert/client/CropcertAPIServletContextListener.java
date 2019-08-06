package cropcert.client;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import cropcert.client.api.APIModule;
import cropcert.client.service.ServiceModule;


public class CropcertAPIServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		
		Injector injector = Guice.createInjector(new JerseyServletModule() {
			@Override
			protected void configureServlets() {
				
				bind(ObjectMapper.class).in(Scopes.SINGLETON);
				
				Map<String, String> props = new HashMap<String, String>();
				props.put("javax.ws.rs.Application", MyApplication.class.getName());
				props.put("jersey.config.server.wadl.disableWadl", "true");
				
				serve("/api/*").with(GuiceContainer.class, props);
			}
		}, new APIModule(), new ServiceModule());
		
		return injector; 
	}
}
