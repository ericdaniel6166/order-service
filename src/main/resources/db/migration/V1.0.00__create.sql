CREATE TABLE IF NOT EXISTS address
(
    id                 SERIAL PRIMARY KEY,
    user_id            BIGINT,
    street             VARCHAR(255),
    city               VARCHAR(255),
    country            VARCHAR(255),
    phone_number       VARCHAR(255),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);


CREATE TABLE IF NOT EXISTS t_order
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            BIGINT,
    status             VARCHAR(255),
    order_detail       TEXT,
    total_amount       NUMERIC(19, 4),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);


CREATE TABLE IF NOT EXISTS order_item
(
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT REFERENCES t_order (id),
    product_id         BIGINT,
    quantity           INTEGER,
    price              NUMERIC(19, 4),
    subtotal           NUMERIC(19, 4),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);


CREATE TABLE IF NOT EXISTS payment
(
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT REFERENCES t_order (id),
    amount             NUMERIC(19, 4),
    status             VARCHAR(255),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);


CREATE TABLE IF NOT EXISTS shipping
(
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT REFERENCES t_order (id),
    address_id         BIGINT REFERENCES address (id),
    status             VARCHAR(255),
    tracking_number    VARCHAR(255),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);


CREATE TABLE IF NOT EXISTS order_status_history
(
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT REFERENCES t_order (id),
    status             VARCHAR(255),
    order_detail       TEXT,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);
