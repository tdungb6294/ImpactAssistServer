ALTER TABLE claims RENAME TO car_claims;

ALTER TABLE car_claims DROP CONSTRAINT claims_user_id_fkey, DROP COLUMN user_id;

CREATE TABLE claims (
    id SERIAL PRIMARY KEY,
    claim_type VARCHAR(30) NOT NULL,
    user_id INT NOT NULL,
    created_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

ALTER TABLE car_claims ADD CONSTRAINT car_claims_claim_id_fkey FOREIGN KEY (id) REFERENCES claims(id);