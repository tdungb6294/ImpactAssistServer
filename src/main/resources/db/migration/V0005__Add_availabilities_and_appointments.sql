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
    start_time      TIME   NOT NULL,
    end_time        TIME   NOT NULL,
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

INSERT INTO users (id, full_name, password, email) VALUES (1, 'John Bosch', 'nothashed', 'john.bosch@gmail.com');
INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (1, 2);
INSERT INTO local_experts(user_id, latitude, longitude, description) VALUES (1, 54.723859, 25.337367, 'Professional garage manager');

INSERT INTO expert_availabilities(local_expert_id, start_time, end_time, day_of_week) VALUES (1, '11:00:00', '12:00:00', 'Monday');
INSERT INTO expert_availabilities(local_expert_id, start_time, end_time, day_of_week) VALUES (1, '12:00:00', '13:00:00', 'Monday');
INSERT INTO expert_availabilities(local_expert_id, start_time, end_time, day_of_week) VALUES (1, '13:00:00', '14:00:00', 'Monday');
INSERT INTO expert_availabilities(local_expert_id, start_time, end_time, day_of_week) VALUES (1, '14:00:00', '15:00:00', 'Monday');


