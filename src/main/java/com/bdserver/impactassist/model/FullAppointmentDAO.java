package com.bdserver.impactassist.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class FullAppointmentDAO {
    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private int availabilityId;
    private int userId;
    private AppointmentStatus appointmentStatus;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeekEnum dayOfWeek;
    private double latitude;
    private double longitude;
    private String fullName;
}
