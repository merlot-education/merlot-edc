package eu.merloteducation.edc.pulltransfer;

import eu.merloteducation.edc.pulltransfer.store.PullTransfer;
import eu.merloteducation.edc.pulltransfer.store.PullTransferIndex;
import okhttp3.Request;
import org.eclipse.edc.connector.spi.transferprocess.TransferProcessService;
import org.eclipse.edc.connector.transfer.spi.types.TransferProcess;
import org.eclipse.edc.connector.transfer.spi.types.TransferProcessStates;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.http.EdcHttpClient;
import org.eclipse.edc.spi.types.domain.edr.EndpointDataReference;

import java.io.IOException;
import java.util.Objects;

public class PullTransferService {

    private final PullTransferIndex pullTransferIndex;

    private final TransferProcessService transferProcessService;

    private final EdcHttpClient httpClient;

    public PullTransferService(PullTransferIndex pullTransferIndex, TransferProcessService transferProcessService, EdcHttpClient httpClient) {
        this.pullTransferIndex = pullTransferIndex;
        this.transferProcessService = transferProcessService;
        this.httpClient = httpClient;
    }

    public void saveTransfer(EndpointDataReference dataReference) {

        var transferProcess = transferProcessService.findById(dataReference.getId());
        if (transferProcess == null) {
            throw new EdcException("Transfer process not found: " + dataReference.getId());
        }

        var transfer = PullTransfer.Builder.newInstance()
                .id(dataReference.getId())
                .contractId(dataReference.getContractId())
                .endpoint(dataReference.getEndpoint())
                .authKey(dataReference.getAuthKey())
                .authCode(dataReference.getAuthCode())
                .build();

        // Validate the received pull transfer
        validateReceivedTransfer(transfer, transferProcess);

        var result = pullTransferIndex.create(transfer);

        if (!result.succeeded()) {
            throw new EdcException("Error saving transfer: " + result.reason().name());
        }
    }

    public TransferDownloadResponse downloadTransfer(String transferProcessId) {
        var pullTransfer = pullTransferIndex.findById(transferProcessId);

        var request = new Request.Builder()
                .addHeader(pullTransfer.getAuthKey(), pullTransfer.getAuthCode())
                .url(pullTransfer.getEndpoint())
                .get()
                .build();

        try (var response = httpClient.execute(request)) {
            if (response.body() == null) {
                return null;
            }

            var body = response.body().bytes();

            var mediaType = Objects.requireNonNull(response.body().contentType()).toString();
            return new TransferDownloadResponse(mediaType, body);

        } catch (IOException e) {
            throw new EdcException("Error accessing pull-transfer endpoint", e);
        }
    }

    private void validateReceivedTransfer(PullTransfer pullTransfer, TransferProcess transferProcess) {

        if (!Objects.equals(transferProcess.getContractId(), pullTransfer.getContractId())) {
            throw new EdcException("Transfer contract mismatch: " + pullTransfer.getContractId());
        }

        if (transferProcess.getState() != TransferProcessStates.STARTED.code()) {
            throw new EdcException("Transfer in invalid status to download: " + transferProcess.getState());
        }
    }

    public record TransferDownloadResponse(String mediaType, byte[] body) {}
}
