package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class AvailabilityDAO {
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOfWeek;
}
