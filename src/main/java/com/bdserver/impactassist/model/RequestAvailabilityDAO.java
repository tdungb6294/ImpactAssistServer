package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RequestAvailabilityDAO {
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOfWeek;
    private int localExpertId;
}
