CREATE TABLE auto_parts_and_services_categories
(
    id            SERIAL PRIMARY KEY,
    category_name VARCHAR(50)    NOT NULL,
    min_price     DECIMAL(10, 2) NOT NULL,
    max_price     DECIMAL(10, 2) NOT NULL
);

CREATE TABLE auto_parts_and_services
(
    id          SERIAL PRIMARY KEY,
    auto_part   VARCHAR(70)    NOT NULL,
    description VARCHAR(300)   NOT NULL,
    min_price   DECIMAL(10, 2) NOT NULL,
    max_price   DECIMAL(10, 2) NOT NULL,
    category_id INT            NOT NULL,
    FOREIGN KEY (category_id) REFERENCES auto_parts_and_services_categories (id)
);