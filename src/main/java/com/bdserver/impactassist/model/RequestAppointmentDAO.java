package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RequestAppointmentDAO {
    private int expertAvailabilityId;
    private LocalDate appointmentDate;
    private String title;
    private String description;
    private Integer userId;
}
