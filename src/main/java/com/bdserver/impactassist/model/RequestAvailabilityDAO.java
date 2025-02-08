package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class RequestAvailabilityDAO {
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private DayOfWeekEnum dayOfWeek;
    @NotNull
    private int localExpertId;
}
