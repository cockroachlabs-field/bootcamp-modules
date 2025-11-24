package io.cockroachdb.bootcamp.patterns.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import tools.jackson.databind.json.JsonMapper;

import io.cockroachdb.bootcamp.common.annotation.ServiceFacade;
import io.cockroachdb.bootcamp.patterns.PurchaseOrderEvent;

@ServiceFacade
public class OutboxChangeFeedListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JsonMapper jsonMapper;

    @KafkaListener(id = "outbox-demo", topics = "orders-outbox", groupId = "training-modules",
            properties = {"spring.json.value.default.type=io.cockroachdb.training.patterns.PurchaseOrderEvent"})
    public void onPurchaseOrderEvent(PurchaseOrderEvent event) {
        logger.info("Received event: {}",
                jsonMapper.writer().writeValueAsString(event));
    }
}
