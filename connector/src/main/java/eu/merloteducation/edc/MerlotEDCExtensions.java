package eu.merloteducation.edc;

import eu.merloteducation.edc.pulltransfer.PullTransferController;
import eu.merloteducation.edc.pulltransfer.PullTransferService;
import eu.merloteducation.edc.pulltransfer.store.memory.InMemoryPullTransferIndex;
import org.eclipse.edc.connector.api.management.configuration.ManagementApiConfiguration;
import org.eclipse.edc.connector.spi.transferprocess.TransferProcessService;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.http.EdcHttpClient;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.web.spi.WebService;

public class MerlotEDCExtensions implements ServiceExtension {

    public static final String NAME = "Merlot EDC Extensions";

    @Inject
    private WebService webService;

    @Inject
    private ManagementApiConfiguration managementApiConfiguration;

    @Inject
    private TransferProcessService transferProcessService;

    @Inject
    private EdcHttpClient httpClient;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();

        var pullTransferIndex = new InMemoryPullTransferIndex();
        var pullTransferService = new PullTransferService(pullTransferIndex, transferProcessService, httpClient);

        var pullTransferController = new PullTransferController(monitor, pullTransferService);
        webService.registerResource(managementApiConfiguration.getContextAlias(), pullTransferController);
        monitor.info(NAME + ": PullTransferController registered");
    }
}
