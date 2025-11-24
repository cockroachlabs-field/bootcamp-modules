package io.cockroachdb.bootcamp.patterns.inbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.resilience.annotation.Retryable;

import io.cockroachdb.bootcamp.common.annotation.ServiceFacade;
import io.cockroachdb.bootcamp.common.annotation.TransactionImplicit;
import io.cockroachdb.bootcamp.domain.model.PurchaseOrder;

@ServiceFacade
public class InboxService {
    @Autowired
    private InboxRepository inboxRepository;

    @TransactionImplicit
    @Retryable(predicate = io.cockroachdb.bootcamp.common.retry.TransientExceptionClassifier.class,
            maxRetries = 5,
            maxDelay = 15_0000,
            multiplier = 1.5)
    public PurchaseOrder placeOrder(PurchaseOrder order) {
        inboxRepository.writeEvent(order, "purchase_order");
        return order;
    }
}
