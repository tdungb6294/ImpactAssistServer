package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDAO {
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
}
