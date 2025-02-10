package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RegisterClaimDAO {
    private Integer userId;
    @NotNull
    @Size(min = 1, max = 60)
    private String carModel;
    @NotNull
    @Size(min = 1, max = 10)
    private String vehicleRegistrationNumber;
    @NotNull
    @Size(min = 17, max = 17)
    private String vehicleIdentificationNumber;
    @NotNull
    @Size(min = 1, max = 10)
    private String odometerMileage;
    @NotNull
    @Size(min = 1, max = 30)
    private String insurancePolicyNumber;
    @NotNull
    @Size(min = 1, max = 50)
    private String insuranceCompany;
    @NotNull
    @PastOrPresent
    private LocalDateTime accidentDatetime;
    @NotNull
    private double locationLongitude;
    @NotNull
    private double locationLatitude;
    @NotNull
    @Size(min = 1, max = 200)
    private String address;
    @Size(min = 1, max = 300)
    private String description;
    @NotNull
    private boolean policeInvolved;
    @Size(min = 1, max = 50)
    private String policeReportNumber;
    @NotNull
    @Size(min = 1, max = 20)
    private String weatherCondition;
    @NotNull
    @Size(min = 1, max = 50)
    private String compensationMethod;
    @Size(min = 1, max = 300)
    private String additionalNotes;
    @NotNull
    private boolean dataManagementConsent;
    @NotNull
    @Size(min = 15, max = 34)
    private String internationalBankAccountNumber;
    @NotNull
    private List<String> documentTypes;
}
