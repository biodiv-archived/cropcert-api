package cropcert.client.service;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TraceabilityService.class).in(Scopes.SINGLETON);
	}
}
