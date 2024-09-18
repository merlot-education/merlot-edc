package eu.merloteducation.edc;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.types.domain.edr.EndpointDataReference;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/v1/pull-transfer/")
public class PullTransferController {

    private final Monitor monitor;

    public PullTransferController(Monitor monitor) {
        this.monitor = monitor;
    }

    @POST
    @Path("receive/")
    public Response receive(EndpointDataReference request) {

        monitor.info("Received Pull Request: " + request.toString());
        return Response.ok().build();
    }
}
