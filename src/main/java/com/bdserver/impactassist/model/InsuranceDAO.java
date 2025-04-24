package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InsuranceDAO {
    private String insuranceCompanyName;
    private String insurancePolicyNumber;
    private String insuranceGreenCardNumber;
    private LocalDate insuranceValidFrom;
    private LocalDate insuranceValidTo;
}
