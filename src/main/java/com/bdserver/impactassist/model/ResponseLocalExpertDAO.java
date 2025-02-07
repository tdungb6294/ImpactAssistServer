package com.bdserver.impactassist.model;

import lombok.Data;

@Data
public class ResponseLocalExpertDAO {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private double longitude;
    private double latitude;
    private String description;
}
