package eu.merloteducation.edc.pulltransfer.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.eclipse.edc.spi.entity.Entity;

import java.util.UUID;

public class PullTransfer extends Entity {

    private String contractId;
    private String endpoint;
    private String authKey;
    private String authCode;

    public String getContractId() {
        return contractId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getAuthCode() {
        return authCode;
    }

    public static class Builder extends Entity.Builder<PullTransfer, Builder> {

        protected Builder(PullTransfer transfer) {
            super(transfer);
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder(new PullTransfer());
        }

        @Override
        public Builder id(String id) {
            entity.id = id;
            return self();
        }

        @Override
        public Builder createdAt(long value) {
            entity.createdAt = value;
            return self();
        }

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public PullTransfer build() {
            super.build();

            if (entity.getId() == null) {
                id(UUID.randomUUID().toString());
            }
            return entity;
        }

        public Builder contractId(String contractId) {
            entity.contractId = contractId;
            return self();
        }

        public Builder endpoint(String endpoint) {
            entity.endpoint = endpoint;
            return self();
        }

        public Builder authKey(String authKey) {
            entity.authKey = authKey;
            return self();
        }

        public Builder authCode(String authCode) {
            entity.authCode = authCode;
            return self();
        }
    }
}
