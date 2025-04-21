CREATE TABLE auto_parts_and_services_categories_translations
(
    id            INT         NOT NULL,
    category_name VARCHAR(50) NOT NULL,
    language_code VARCHAR(5)  NOT NULL,
    PRIMARY KEY (id, language_code),
    FOREIGN KEY (id) REFERENCES auto_parts_and_services_categories (id) ON DELETE CASCADE
);

CREATE TABLE auto_parts_and_services_translations
(
    id            INT          NOT NULL,
    auto_part     VARCHAR(100) NOT NULL,
    description   VARCHAR(500) NOT NULL,
    language_code VARCHAR(5)   NOT NULL,
    PRIMARY KEY (id, language_code),
    FOREIGN KEY (id) REFERENCES auto_parts_and_services (id) ON DELETE CASCADE
);