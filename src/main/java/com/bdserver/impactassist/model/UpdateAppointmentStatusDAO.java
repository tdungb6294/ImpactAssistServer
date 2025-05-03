package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateAppointmentStatusDAO {
    private int appointmentId;
    private AppointmentStatus appointmentStatus;
}
