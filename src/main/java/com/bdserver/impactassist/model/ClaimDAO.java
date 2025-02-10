package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClaimDAO {
    private int id;
    private int userId;
    private String carModel;
    private String vehicleRegistrationNumber;
    private String vehicleIdentificationNumber;
    private String odometerMileage;
    private String insurancePolicyNumber;
    private String insuranceCompany;
    private LocalDateTime accidentDatetime;
    private double locationLongitude;
    private double locationLatitude;
    private String address;
    private String description;
    private boolean policeInvolved;
    private String policeReportNumber;
    private String weatherCondition;
    private List<String> claimAccidentImageUrls;
    private List<PartialClaimDocumentDAO> claimAccidentDocuments;
    private String compensationMethod;
    private String additionalNotes;
    private boolean dataManagementConsent;
    private String internationalBankAccountNumber;
}
