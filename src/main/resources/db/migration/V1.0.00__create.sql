create table if not exists address
(
    id                 serial primary key,
    user_id            bigint,
    street             varchar(255),
    city               varchar(255),
    country            varchar(255),
    phone_number       varchar(255),
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);


create table if not exists t_order
(
    id                 bigserial primary key,
    user_id            bigint,
    status             varchar(255),
    fail_reason        text,
    total_amount       numeric(19, 4),
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);


create table if not exists order_item
(
    id                 bigserial primary key,
    order_id           bigint references t_order (id),
    product_id         bigint,
    quantity           integer,
    price              numeric(19, 4),
    subtotal           numeric(19, 4),
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);


create table if not exists payment
(
    id                 bigserial primary key,
    order_id           bigint references t_order (id),
    amount             numeric(19, 4),
    status             varchar(255),
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);


create table if not exists shipping
(
    id                 bigserial primary key,
    order_id           bigint references t_order (id),
    address_id         bigint references address (id),
    status             varchar(255),
    tracking_number    varchar(255),
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);


create table if not exists order_status_history
(
    id                 bigserial primary key,
    order_id           bigint references t_order (id),
    status             varchar(255),
    fail_reason        text,
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);
