CREATE TABLE local_experts
(
    user_id     INT NOT NULL,
    longitude   DOUBLE PRECISION,
    latitude    DOUBLE PRECISION,
    description TEXT,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);