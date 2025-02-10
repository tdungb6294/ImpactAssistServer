package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartialClaimDocumentDAO {
    private String url;
    private String documentType;
}
