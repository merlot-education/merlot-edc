package eu.merloteducation.edc.pulltransfer;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.types.domain.edr.EndpointDataReference;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/merlot/pull-transfers/")
public class PullTransferController {

    private final Monitor monitor;

    private final PullTransferService pullTransferService;

    public PullTransferController(Monitor monitor, PullTransferService pullTransferService) {
        this.monitor = monitor;
        this.pullTransferService = pullTransferService;
    }

    @POST
    @Path("receive/")
    public Response receive(EndpointDataReference dataReference) {
        monitor.debug("Received Pull Transfer: " + dataReference.getId());

        pullTransferService.saveTransfer(dataReference);

        return Response.ok().build();
    }

    @GET
    @Path("{id}/download/")
    public Response download(@PathParam("id") String transferProcessId) {
        monitor.debug("Pull Transfer Download: " + transferProcessId);

        var response = pullTransferService.downloadTransfer(transferProcessId);

        if (response == null) {
            return Response.noContent().build();
        } else if (response.mediaType() == null) {
            return Response.ok(response.body()).build();
        } else {
            return Response.ok(response.body()).type(response.mediaType()).build();
        }
    }
}
