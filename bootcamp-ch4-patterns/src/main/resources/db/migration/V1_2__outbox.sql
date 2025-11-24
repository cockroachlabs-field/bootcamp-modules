-- From a performance standpoint there should be one outbox table per aggregate that
-- will reduce the number of change feeds per range. In this demo however, there's
-- only one outbox table with a discriminator column for the aggregate type.

create table if not exists outbox
(
    id             uuid as ((payload ->> 'id')::UUID) stored,
    aggregate_type varchar(32) not null,
    payload        jsonb       not null,

    primary key (id)
);

alter table outbox set (ttl_expire_after = '1 hour');
