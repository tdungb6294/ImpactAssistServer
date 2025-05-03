package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RegisterCarClaimDAO {
    private int id;
    @NotBlank
    @Size(min = 1, max = 60)
    private String carModel;
    @NotBlank
    @Size(min = 1, max = 10)
    private String vehicleRegistrationNumber;
    @NotBlank
    @Size(min = 17, max = 17)
    private String vehicleIdentificationNumber;
    @NotBlank
    @Size(min = 1, max = 10)
    private String odometerMileage;
    @NotBlank
    @Size(min = 1, max = 30)
    private String insurancePolicyNumber;
    @NotBlank
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
    @NotBlank
    @Size(min = 1, max = 20)
    private String weatherCondition;
    @NotBlank
    @Size(min = 1, max = 50)
    private String compensationMethod;
    @Size(min = 1, max = 300)
    private String additionalNotes;
    @NotNull
    private boolean dataManagementConsent;
    @NotBlank
    @Size(min = 15, max = 34)
    private String internationalBankAccountNumber;
    @NotNull
    private List<String> documentTypes;
}
