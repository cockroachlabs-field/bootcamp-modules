package io.cockroachdb.bootcamp.patterns;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.cockroachdb.bootcamp.Chapter4Application;
import io.cockroachdb.bootcamp.domain.model.Product;
import io.cockroachdb.bootcamp.domain.model.PurchaseOrder;
import io.cockroachdb.bootcamp.domain.test.AbstractIntegrationTest;
import io.cockroachdb.bootcamp.patterns.inbox.InboxService;

@ActiveProfiles({"domain"})
@SpringBootTest(classes = {Chapter4Application.class})
public class InboxPatternTest extends AbstractIntegrationTest {
    @Autowired
    private InboxService inboxService;

    @Autowired
    @Qualifier("inboxOrderService")
    private OrderService orderService;

    @Order(1)
    @Test
    public void whenPlaceOneOrder_thenExpectInboxEvent() {
        createCustomersAndProducts(10, 10);

        testDataService.withRandomCustomersAndProducts(10, 10,
                (customers, products) -> {
                    Assertions.assertFalse(customers.isEmpty(), "No customers");
                    Assertions.assertFalse(products.isEmpty(), "No products");

                    Product product = products.getFirst();

                    PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                            .withGeneratedId()
                            .withCustomer(customers.getFirst())
                            .andOrderItem()
                            .withProductId(product.getId())
                            .withProductSku(product.getSku())
                            .withUnitPrice(product.getPrice())
                            .withQuantity(1)
                            .then()
                            .build();

                    return inboxService.placeOrder(purchaseOrder);
                });
    }
}
