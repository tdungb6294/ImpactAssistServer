package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ObjectClaimDAO {
    private int id;
    private String objectType;
    private String objectMaterial;
    private ObjectOwnership objectOwnership;
    private String damageToObjectDescription;
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
    private ClaimStatus claimStatus;
}
