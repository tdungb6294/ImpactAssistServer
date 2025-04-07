package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.AppointmentDAO;
import com.bdserver.impactassist.model.AppointmentStatusEnum;
import com.bdserver.impactassist.model.RequestAppointmentDAO;
import com.bdserver.impactassist.model.UpdateAppointmentStatusDAO;
import com.bdserver.impactassist.service.AppointmentService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping()
    Integer registerAppointment(@Valid @RequestBody RequestAppointmentDAO request) throws BadRequestException {
        return appointmentService.registerAppointment(request);
    }

    @GetMapping
    List<AppointmentDAO> getAllAppointments(@RequestParam(required = false) Integer userId,
                                            @RequestParam(required = false) Integer expertId,
                                            @RequestParam(required = false) List<AppointmentStatusEnum> appointmentStatus,
                                            @RequestParam(defaultValue = "50") Integer limit,
                                            @RequestParam(defaultValue = "0") Integer offset,
                                            @RequestParam(required = false) List<LocalDate> date) {
        if (userId != null) {
            return appointmentService.getAppointmentsByUserId(userId);
        }
        if (expertId != null) {
            return appointmentService.getAppointmentsByExpertId(appointmentStatus, limit, offset, expertId, date);
        }
        return appointmentService.getAllAppointmentsAuthenticated();
    }

    @GetMapping("/{id}")
    AppointmentDAO getAppointmentsByUserId(@PathVariable int id) {
        return appointmentService.getAppointmentById(id);
    }

    //TODO make this validate expert, because now we have any expert
    @PreAuthorize("hasAuthority('LOCAL_EXPERT')")
    @PutMapping
    void updateAppointment(@RequestBody UpdateAppointmentStatusDAO updateAppointmentStatusDAO) {
        appointmentService.updateAppointmentStatus(updateAppointmentStatusDAO);
    }
}
