package io.cockroachdb.bootcamp.transactions;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.cockroachdb.bootcamp.domain.model.Customer;
import io.cockroachdb.bootcamp.domain.model.Product;
import io.cockroachdb.bootcamp.domain.model.PurchaseOrder;
import io.cockroachdb.bootcamp.domain.model.ShipmentStatus;
import io.cockroachdb.bootcamp.domain.model.Simulation;

public interface OrderService {
    Page<Product> findProducts(Pageable pageable);

    Page<Customer> findCustomers(Pageable pageable);

    Page<PurchaseOrder> findOrders(Pageable pageable);

    Optional<PurchaseOrder> findOrderById(UUID id);

    PurchaseOrder placeOrder(PurchaseOrder order);

    PurchaseOrder placeOrderWithValidation(PurchaseOrder order);

    void updateOrder(UUID id, ShipmentStatus preCondition, ShipmentStatus postCondition,
                     Simulation simulation);
}
