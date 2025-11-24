create table if not exists inbox
(
    id             uuid as ((payload ->> 'id')::UUID) stored,
    aggregate_type varchar(32) not null,
    payload        jsonb       not null,

    primary key (id)
);

alter table inbox set (ttl_expire_after = '1 hour');