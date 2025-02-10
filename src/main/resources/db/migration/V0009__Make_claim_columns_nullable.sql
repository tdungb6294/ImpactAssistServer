ALTER TABLE claims
    ALTER COLUMN description DROP NOT NULL,
    ALTER COLUMN additional_notes DROP NOT NULL;
