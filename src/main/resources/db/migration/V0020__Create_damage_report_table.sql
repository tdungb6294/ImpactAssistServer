CREATE TABLE damage_reports
(
    report_id SERIAL PRIMARY KEY,
    user_id   INT NOT NULL,
    claim_id  INT NOT NULl,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (claim_id) REFERENCES claims (id)
);

CREATE TABLE damage_report_data
(
    report_id            INT NOT NULL,
    auto_part_service_id INT NOT NULL,
    PRIMARY KEY (report_id, auto_part_service_id),
    FOREIGN KEY (report_id) REFERENCES damage_reports (report_id),
    FOREIGN KEY (auto_part_service_id) REFERENCES auto_parts_and_services (id)
);