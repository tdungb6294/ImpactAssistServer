package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RequestCompensationDAO {
    @NotNull
    private int reportId;
    @NotNull
    private BigDecimal compensationAmount;
}
