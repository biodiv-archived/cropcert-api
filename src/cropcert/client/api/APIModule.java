package cropcert.client.api;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class APIModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TraceabilityApi.class).in(Scopes.SINGLETON);
		bind(UserApi.class).in(Scopes.SINGLETON);
	}
}
