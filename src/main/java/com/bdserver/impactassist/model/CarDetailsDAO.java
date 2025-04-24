package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarDetailsDAO {
    @NotNull
    private InsurerDAO insurer;
    @NotNull
    private CarDAO car;
    @NotNull
    private InsuranceDAO insurance;
    @NotNull
    private boolean insurancePolicyCoverage;
    @NotNull
    private DriverDAO driver;
    @NotNull
    private String damageDescription;
    @NotNull
    private String circumstance;
}
