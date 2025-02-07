CREATE TYPE day_of_week AS ENUM (
    'Sunday',
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday'
);

CREATE TABLE expert_availabilities
(
    id              SERIAL PRIMARY KEY,
    local_expert_id INT         NOT NULL,
    start_time      TIMESTAMP   NOT NULL,
    end_time        TIMESTAMP   NOT NULL,
    day_of_week     day_of_week NOT NULL,
    FOREIGN KEY (local_expert_id) REFERENCES users (id)
);

CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    local_expert_id INT NOT NULL,
    user_id INT NOT NULL,
    availability_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    appointment_time TIMESTAMP NOT NULL,
    appointment_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (availability_id) REFERENCES expert_availabilities (id),
    FOREIGN KEY (local_expert_id) REFERENCES users (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);