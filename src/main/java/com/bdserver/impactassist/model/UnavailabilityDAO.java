package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UnavailabilityDAO {
    private LocalDate date;
    private int availabilityId;
}
