
package com.github.psamsotha.loadonstartup;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("demo")
public class DemoResource {
	
	@GET
	public String get() {
		return "GET";
	}
	
	@GET
	@Path("path")
	public String getPath() {
		return "GET path";
	}
	
	@POST
	public String post(String post) {
		return post;
	}
}
