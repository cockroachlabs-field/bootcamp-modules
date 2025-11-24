package io.cockroachdb.bootcamp.transactions;

import java.math.BigDecimal;
import java.util.UUID;

public interface InventoryService {
    void validateProductInventory(UUID id, BigDecimal price, int quantity);
}
