package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class AutoPartsAndServicesDAO {
    private int id;
    private String autoPart;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String categoryName;
}
