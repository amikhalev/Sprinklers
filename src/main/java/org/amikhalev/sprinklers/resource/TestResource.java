package org.amikhalev.sprinklers.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by alex on 5/3/15.
 */
@Path("/test")
public class TestResource {
    @GET
    @Produces("text/plain")
    public String test() {
        return "Test!";
    }
}
