package eu.merloteducation.edc.pulltransfer.store.memory;

import eu.merloteducation.edc.pulltransfer.store.PullTransfer;
import eu.merloteducation.edc.pulltransfer.store.PullTransferIndex;
import org.eclipse.edc.spi.result.StoreResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.String.format;

public class InMemoryPullTransferIndex implements PullTransferIndex {

    private final Map<String, PullTransfer> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock;

    public InMemoryPullTransferIndex() {
        lock = new ReentrantReadWriteLock(true);
    }

    @Override
    public PullTransfer findById(String transferId) {
        lock.readLock().lock();
        try {
            return cache.values().stream()
                    .filter(transfer -> transfer.getId().equals(transferId))
                    .findFirst()
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public StoreResult<Void> create(PullTransfer transfer) {
        lock.writeLock().lock();
        try {
            var id = transfer.getId();
            if (cache.containsKey(id)) {
                return StoreResult.alreadyExists(format(PULL_TRANSFER_EXISTS_TEMPLATE, id));
            }
            cache.put(transfer.getId(), transfer);
        } finally {
            lock.writeLock().unlock();
        }
        return StoreResult.success();
    }
}
