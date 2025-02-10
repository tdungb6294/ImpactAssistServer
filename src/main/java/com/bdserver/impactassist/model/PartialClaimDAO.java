package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PartialClaimDAO {
    private int id;
    private String carModel;
    private LocalDateTime accidentDatetime;
    private String address;
}
