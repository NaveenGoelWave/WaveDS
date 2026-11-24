-- Dropdown values that can change over time (customer type, company)
CREATE TABLE lookup_value (
    id         BIGSERIAL PRIMARY KEY,
    category   VARCHAR(50)  NOT NULL,
    label      VARCHAR(150) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    sort_order INT          NOT NULL DEFAULT 0,
    CONSTRAINT uq_lookup_category_label UNIQUE (category, label)
);

CREATE TABLE customer (
    id               BIGSERIAL PRIMARY KEY,
    customer_name    VARCHAR(200) NOT NULL,
    email            VARCHAR(200),
    contact_number   VARCHAR(15)  NOT NULL,
    alternate_number VARCHAR(15),
    address          VARCHAR(500) NOT NULL,
    city             VARCHAR(100) NOT NULL,
    area_sector      VARCHAR(150) NOT NULL,
    zone             VARCHAR(30)  NOT NULL,
    site_type        VARCHAR(30)  NOT NULL,
    pincode          VARCHAR(6)   NOT NULL,
    customer_type_id BIGINT       NOT NULL REFERENCES lookup_value (id),
    brand            VARCHAR(30)  NOT NULL,
    company_id       BIGINT       NOT NULL REFERENCES lookup_value (id),
    gem_order_number VARCHAR(100),
    old_customer_id  VARCHAR(100),
    start_date       DATE         NOT NULL,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_customer_contact_number ON customer (contact_number);
CREATE INDEX idx_customer_city           ON customer (city);
CREATE INDEX idx_customer_zone           ON customer (zone);
CREATE INDEX idx_customer_brand          ON customer (brand);
CREATE INDEX idx_customer_created_at     ON customer (created_at DESC);
