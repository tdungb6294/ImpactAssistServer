CREATE TABLE claims
(
    id                                SERIAL PRIMARY KEY,
    user_id                           INT              NOT NULL,
    car_model                         VARCHAR(60)      NOT NULL,
    vehicle_registration_number       VARCHAR(10)      NOT NULL,
    vehicle_identification_number     VARCHAR(17)      NOT NULL,
    odometer_mileage                  VARCHAR(10)      NOT NULL,
    insurance_policy_number           VARCHAR(30)      NOT NULL,
    insurance_company                 VARCHAR(50)      NOT NULL,
    accident_datetime                 TIMESTAMP        NOT NULL,
    location_longitude                DOUBLE PRECISION NOT NULL,
    location_latitude                 DOUBLE PRECISION NOT NULL,
    address                           VARCHAR(200)     NOT NULL,
    description                       TEXT             NOT NULL,
    police_involved                   BOOLEAN          NOT NULL,
    police_report_number              VARCHAR(50),
    weather_condition                 VARCHAR(20)      NOT NULL,
    compensation_method               VARCHAR(50)      NOT NULL,
    additional_notes                  TEXT             NOT NULL,
    data_management_consent           BOOLEAN          NOT NULL,
    international_bank_account_number VARCHAR(34)      NOT NULL,
    created_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TRIGGER update_claims_updated_at
BEFORE UPDATE ON claims
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE claims_accident_images (
    id SERIAL PRIMARY KEY,
    claim_id INT NOT NULL,
    file_name VARCHAR(100) NOT NULL,
    unique_file_identifier UUID UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL,
    FOREIGN KEY (claim_id) REFERENCES claims (id)
);

CREATE TABLE claims_accident_documents (
    id SERIAL PRIMARY KEY,
    claim_id INT NOT NULL,
    file_name VARCHAR(100) NOT NULL,
    unique_file_identifier UUID UNIQUE NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL,
    FOREIGN KEY (claim_id) REFERENCES claims (id)
);
