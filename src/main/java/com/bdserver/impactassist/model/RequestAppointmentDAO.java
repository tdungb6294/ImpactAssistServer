package com.bdserver.impactassist.model;

import com.bdserver.impactassist.validator.FutureWithin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class RequestAppointmentDAO {
    @NotNull
    private int expertAvailabilityId;
    @NotNull
    @FutureWithin(message = "Appointment date must be within the next 30 days")
    private LocalDate appointmentDate;
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;
    @NotBlank
    @Size(min = 1, max = 300)
    private String description;
    private Integer userId;
}
