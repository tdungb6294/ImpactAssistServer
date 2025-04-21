package com.bdserver.impactassist.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DamageReportDAO {
    private Integer reportId;
    private String fullName;
    private BigDecimal estimatedMinPriceWithoutService;
    private BigDecimal estimatedMaxPriceWithoutService;
    private BigDecimal estimatedMinPriceWithService;
    private BigDecimal estimatedMaxPriceWithService;
}
