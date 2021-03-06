package fr.zapho.sandbox.jee.timers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Singleton
@Path("/timers")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class BaseResource {

    @Inject
    Timer1 timer1;

    @GET
    public Response manageTimer1(@QueryParam("op") String operation) {
        switch (operation) {
            case "read":
                return Response.ok(timer1.getTimers().stream().map(t -> t.getInfo().toString()).collect(Collectors.joining(","))).build();
            case "readall":
                return Response.ok(timer1.getAllTimers().stream().map(t -> t.getInfo().toString()).collect(Collectors.joining(","))).build();
            case "stopdirect":
                timer1.cancelTimerDirect();
                return Response.ok().build();
            case "stoploop":
                timer1.cancelTimerLoop();
                return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
