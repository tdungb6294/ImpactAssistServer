package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestUpdateClaimStatus {
    @NotNull
    private Integer claimId;
    @NotNull
    private ClaimStatus status;
}
