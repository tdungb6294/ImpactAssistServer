ALTER TABLE claims
    ADD COLUMN shared_id INT REFERENCES local_experts (user_id) ON DELETE SET NULL;
