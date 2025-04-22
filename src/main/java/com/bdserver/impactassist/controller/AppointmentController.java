package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.AppointmentStatus;
import com.bdserver.impactassist.model.FullAppointmentDAO;
import com.bdserver.impactassist.model.RequestAppointmentDAO;
import com.bdserver.impactassist.model.UpdateAppointmentStatusDAO;
import com.bdserver.impactassist.service.AppointmentService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping()
    public Integer registerAppointment(@Valid @RequestBody RequestAppointmentDAO request) throws BadRequestException {
        return appointmentService.registerAppointment(request);
    }

    @GetMapping
    public Map<String, Object> getAllAppointments(@RequestParam(required = false) Integer userId,
                                                  @RequestParam(required = false) Boolean expert,
                                                  @RequestParam(required = false) List<AppointmentStatus> appointmentStatus,
                                                  @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "6") int size,
                                                  @RequestParam(required = false) List<LocalDate> date) {
        if (expert != null && expert) {
            return appointmentService.getAppointmentsByExpertId(appointmentStatus, date, page, size);
        }
        if (userId != null) {
            return appointmentService.getAppointmentsByUserId(userId, page, size);
        }
        return appointmentService.getAllAppointmentsAuthenticated(page, size);
    }

    @GetMapping("/{id}")
    public FullAppointmentDAO getAppointmentsByUserId(@PathVariable int id) {
        return appointmentService.getAppointmentById(id);
    }

    @PreAuthorize("hasAuthority('LOCAL_EXPERT')")
    @PutMapping
    public void updateAppointment(@RequestBody UpdateAppointmentStatusDAO updateAppointmentStatusDAO) throws BadRequestException {
        appointmentService.updateAppointmentStatus(updateAppointmentStatusDAO);
    }
}
