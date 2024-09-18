package eu.merloteducation.edc;

import org.eclipse.edc.connector.api.control.configuration.ControlApiConfiguration;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.web.spi.WebService;

public class EDCExtension implements ServiceExtension {

    public static final String NAME = "Merlot EDC Extension";

    @Inject
    private WebService webService;

    @Inject
    private ControlApiConfiguration controlApiConfiguration;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();

        var pullTransferController = new PullTransferController(monitor);
        webService.registerResource(controlApiConfiguration.getContextAlias(), pullTransferController);
        monitor.info(NAME + ": PullTransferController registered");
    }
}
