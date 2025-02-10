package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;

//TODO claim compensation
@Data
public class ClaimCompensationDAO {
    private int id;
    private int claimId;
    private LocalDateTime compensationDate;
    private int localExpertId;
}
