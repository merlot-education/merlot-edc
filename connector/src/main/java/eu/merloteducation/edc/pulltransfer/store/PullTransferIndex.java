package eu.merloteducation.edc.pulltransfer.store;

import org.eclipse.edc.runtime.metamodel.annotation.ExtensionPoint;
import org.eclipse.edc.spi.result.StoreResult;

@ExtensionPoint
public interface PullTransferIndex {

    String PULL_TRANSFER_EXISTS_TEMPLATE = "Pull Transfer with ID %s already exists";

    PullTransfer findById(String transferId);

    StoreResult<Void> create(PullTransfer transfer);
}
