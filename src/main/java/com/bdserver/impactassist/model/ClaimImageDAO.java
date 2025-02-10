package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ClaimImageDAO {
    private UUID uniqueFileIdentifier;
    private String fileName;
}
