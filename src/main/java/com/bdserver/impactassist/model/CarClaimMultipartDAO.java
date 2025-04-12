package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarClaimMultipartDAO {
    private int id;
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
    private String compensationMethod;
    private String additionalNotes;
    private boolean dataManagementConsent;
    private String internationalBankAccountNumber;
    private List<String> claimAccidentImages;
    private List<String> claimAccidentDocuments;
}
