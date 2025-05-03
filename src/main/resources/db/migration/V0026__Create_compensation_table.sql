CREATE TABLE compensations
(
    id                  INT PRIMARY KEY,
    compensation_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id) REFERENCES damage_reports (report_id)
)