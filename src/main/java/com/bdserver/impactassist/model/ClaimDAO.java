package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimDAO {
    private int userId;
    private String claimType;
}
