package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PartialCarClaimDAO {
    private int id;
    private String carModel;
    private LocalDateTime accidentDatetime;
    private String address;
    private ClaimStatus claimStatus;
}
