ALTER TABLE appointments
    ALTER COLUMN appointment_time SET DATA TYPE DATE;

ALTER TABLE appointments
    DROP CONSTRAINT appointments_local_expert_id_fkey,
    DROP COLUMN local_expert_id;