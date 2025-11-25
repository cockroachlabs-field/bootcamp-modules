package io.cockroachdb.bootcamp.patterns.inbox;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.resilience.annotation.Retryable;

import io.cockroachdb.bootcamp.domain.annotation.ServiceFacade;
import io.cockroachdb.bootcamp.domain.annotation.TransactionExplicit;
import io.cockroachdb.bootcamp.domain.model.Product;
import io.cockroachdb.bootcamp.domain.model.PurchaseOrder;
import io.cockroachdb.bootcamp.domain.model.ShipmentStatus;
import io.cockroachdb.bootcamp.domain.repository.OrderRepository;
import io.cockroachdb.bootcamp.domain.repository.ProductRepository;
import io.cockroachdb.bootcamp.domain.retry.TransientExceptionClassifier;
import io.cockroachdb.bootcamp.domain.util.AssertUtils;
import io.cockroachdb.bootcamp.patterns.BusinessException;
import io.cockroachdb.bootcamp.patterns.OrderService;

@ServiceFacade
public class InboxOrderService implements OrderService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @TransactionExplicit
    @Retryable(predicate = TransientExceptionClassifier.class,
            maxRetries = 5,
            maxDelay = 15_0000,
            multiplier = 1.5)
    public PurchaseOrder placeOrder(PurchaseOrder order) throws BusinessException {
        AssertUtils.assertReadWriteTransaction();

        try {
            // Update product inventories for each line item
            order.getOrderItems().forEach(orderItem -> {
                Product product = productRepository.getReferenceById(
                        Objects.requireNonNull(orderItem.getProduct().getId()));
                product.addInventoryQuantity(-orderItem.getQuantity());
            });

            order.setStatus(ShipmentStatus.placed);
            order.setTotalPrice(order.subTotal());

            orderRepository.saveAndFlush(order); // flush to surface any constraint violations

            return order;
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Constraint violation", e);
        }
    }
}
