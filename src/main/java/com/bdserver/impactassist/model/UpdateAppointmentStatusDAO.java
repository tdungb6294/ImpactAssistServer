package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateAppointmentStatusDAO {
    private int appointmentId;
    private AppointmentStatus appointmentStatus;
}
