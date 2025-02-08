ALTER TABLE appointments
    ADD CONSTRAINT unique_appointment_date_and_availability UNIQUE (appointment_time, availability_id);