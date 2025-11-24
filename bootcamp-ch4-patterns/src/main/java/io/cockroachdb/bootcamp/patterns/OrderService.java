package io.cockroachdb.bootcamp.patterns;

import io.cockroachdb.bootcamp.domain.model.PurchaseOrder;

public interface OrderService {
    PurchaseOrder placeOrder(PurchaseOrder order);
}
