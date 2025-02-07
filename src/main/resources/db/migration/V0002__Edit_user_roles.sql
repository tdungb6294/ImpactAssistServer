ALTER TABLE users DROP COLUMN roles;

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('LOCAL_EXPERT');
INSERT INTO roles (name) VALUES ('INSURANCE_COMPANY');
INSERT INTO roles (name) VALUES ('ADMIN');
