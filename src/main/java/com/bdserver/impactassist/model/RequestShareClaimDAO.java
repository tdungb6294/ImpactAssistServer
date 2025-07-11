package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestShareClaimDAO {
    @NotNull
    private int claimId;
    @NotNull
    private int localExpertId;
}
