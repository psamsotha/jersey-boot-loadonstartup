
package com.github.psamsotha.loadonstartup;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig(String applicationPath) {
		register(DemoResource.class);
		register(new EndpointLoggingListener(applicationPath));
	}
}
