package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverDAO {
    private String familyName;
    private String name;
    private String address;
    private String postalCode;
    private String country;
    private String contacts;
    private String drivingLicenceNumber;
    private String drivingLicenceCategory;
    private LocalDate drivingLicenceExpirationDate;
}
