set cluster setting kv.rangefeed.enabled = true;

create changefeed into '${cdc-sink-url}?topic_name=orders-inbox'
with diff as
         select id             as aggregate_id,
                aggregate_type as aggregate_type,
                event_op()     as event_type,
                payload
         from inbox
         where event_op() != 'delete'
           and aggregate_type = 'purchase_order';

create changefeed into '${cdc-sink-url}?topic_name=orders-outbox'
with diff as
         select id             as aggregate_id,
                aggregate_type as aggregate_type,
                event_op()     as event_type,
                payload
         from outbox
         where event_op() != 'delete'
           and aggregate_type = 'purchase_order';
