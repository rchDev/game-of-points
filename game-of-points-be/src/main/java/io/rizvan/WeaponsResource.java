package io.rizvan;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/weapons")
public class WeaponsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }
}
