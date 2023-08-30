create table if not exists t_order
(
    id                 bigserial
        primary key,
    order_number       varchar(255),
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6)
);

create table if not exists order_line_item
(
    id                 bigserial
        primary key,
    sku_code           varchar(255),
    price              numeric(19, 4),
    quantity           integer,
    order_id           bigint,
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6),
    constraint order_line_item_order_id_fkey
        foreign key (order_id)
            references t_order (id)
            on update cascade on delete cascade
);


