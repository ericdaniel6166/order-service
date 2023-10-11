CREATE TABLE IF NOT EXISTS t_order
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            BIGINT       NOT NULL,
    status             VARCHAR(255) NOT NULL,
    order_detail       TEXT,
    total_amount       NUMERIC(19, 4),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS order_status_history
(
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT       NOT NULL,
    status             VARCHAR(255) NOT NULL,
    order_detail       TEXT,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);
