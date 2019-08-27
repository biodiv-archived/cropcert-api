package cropcert.client.filter;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class FilterModule extends AbstractModule {

	
	@Override
	protected void configure() {
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(TokenAndUserAuthenticated.class), new SecurityInterceptor());
	}
}
