package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RequestDamageReportDAO {
    private Integer reportId;
    @NotNull
    private Integer claimId;
    @NotNull
    @NotEmpty
    private List<Integer> autoPartsAndServices;
}
