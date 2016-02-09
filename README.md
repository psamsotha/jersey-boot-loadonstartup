
Spring Boot issue https://github.com/spring-projects/spring-boot/issues/5100

The problem is that Jersey application doesn't load until the first request is made.
This may not be a problem for some applications, but some things that you expect to 
occur on startup, like startup application event listeners being triggered
won't get triggered until the first request.

This example application has an example. The fix is to just set the load on start property
of the Jersey `ServletRegistrationBean`, but that means we need to override the one
registered by Spring Boot. You can see in the example application, in the 
`JerseyApplication`, the configuration does just that. But most of the code is copied
straight from the `JerseyAutoConfiguration` to get the same behavior. This could
easily be avoided with a new property in the `JerseyProperties` that allows us to
set the load on start up. Setting the default value to `-1` would have the same effect as
if the property was not present. This is per the semantics and specification of the
`load-on-startup`. 

So if the property is not set, than no behavior would be changed.
You can see this affect in the example. Just change the `setLoadOnStartup`. The
affect can be seen by observing the timing of the endpoint logging listener that
logs the endpoints. With the load on startup set to `-1`, the listener (and ultimateley)
the entire Jersey application doesn't load until the first request is made. You can make a
request to the enpoint `http://localhost:8080/api/demo` to see. If you comment out 
the line with the load on startup up call, it will have the same effect. Then change to 
`1`, and you will see that the application logging does occur on startup.

The application can be run with Spring Boot Maven plugin `mvn package spring-boot:run`