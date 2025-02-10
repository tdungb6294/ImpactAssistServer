package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ClaimDocumentDAO {
    private UUID uniqueFileIdentifier;
    private String fileName;
    private String documentType;
}
