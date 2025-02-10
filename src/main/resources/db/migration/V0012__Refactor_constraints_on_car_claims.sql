ALTER TABLE claims_accident_documents DROP CONSTRAINT claims_accident_documents_claim_id_fkey;
ALTER TABLE claims_accident_documents ADD CONSTRAINT claims_accident_documents_claim_id_fkey FOREIGN KEY (claim_id) REFERENCES claims(id);

ALTER TABLE claims_accident_images DROP CONSTRAINT claims_accident_images_claim_id_fkey;
ALTER TABLE claims_accident_images ADD CONSTRAINT claims_accident_images_claim_id_fkey FOREIGN KEY (claim_id) REFERENCES claims(id);
