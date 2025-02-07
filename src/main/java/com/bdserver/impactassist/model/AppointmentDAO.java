package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDAO {
    private int id;
    private String title;
    private String description;
    private LocalDateTime date;
    private int availabilityId;
    private int userId;
    private int localExpertId;
    private String status;
    private AppointmentStatusEnum appointmentStatus;
}
