package org.acme.counter;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/counter")
public class CounterResource {

    @Inject
    CounterService counterService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int get() {
        return counterService.get();
    }

    @POST
    @Path("/increment")
    @Produces(MediaType.TEXT_PLAIN)
    public int increment() {
        return counterService.increment();
    }

    @POST
    @Path("/decrement")
    @Produces(MediaType.TEXT_PLAIN)
    public int decrement() {
        return counterService.decrement();
    }

    @POST
    @Path("/reset")
    @Produces(MediaType.TEXT_PLAIN)
    public int reset() {
        return counterService.reset();
    }
}
