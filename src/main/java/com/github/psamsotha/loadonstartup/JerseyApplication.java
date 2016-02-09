
package com.github.psamsotha.loadonstartup;

import java.util.Map.Entry;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.context.embedded.RegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JerseyApplication {
	
	 public static void main(String[] args) {
        SpringApplication.run(JerseyApplication.class, args);
    }

    @Bean
    public ResourceConfig getResourceConfig(JerseyProperties jerseyProperties) {
        return new JerseyConfig(jerseyProperties.getApplicationPath());
    }

    @Bean
    public ServletRegistrationBean jerseyServletRegistration(
        JerseyProperties jerseyProperties, ResourceConfig config) {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new ServletContainer(config), 
                parseApplicationPath(jerseyProperties.getApplicationPath())
        );
        addInitParameters(registration, jerseyProperties);
        registration.setName(JerseyConfig.class.getName());
		// if we use a negative value for the load on startup, it will be business as usual,
		// i.e. the app won't load. If we change it to 1, the app will load.
		// You can see the difference with the `EndpointLoggingListener` which is meant
		// to log the endpoints when the app loads, but with the default behavior,
		// the app doesn't load until the first request. You can see this behavior
		// by switching back and forth between, 1 and -1.
        registration.setLoadOnStartup(-1);
        return registration;
    }

    private static String parseApplicationPath(String applicationPath) {
        if (!applicationPath.startsWith("/")) {
            applicationPath = "/" + applicationPath;
        }
        return applicationPath.equals("/") ? "/*" : applicationPath + "/*";
    }

    private void addInitParameters(RegistrationBean registration, JerseyProperties jersey) {
        for (Entry<String, String> entry : jersey.getInit().entrySet()) {
            registration.addInitParameter(entry.getKey(), entry.getValue());
        }
    }
}
