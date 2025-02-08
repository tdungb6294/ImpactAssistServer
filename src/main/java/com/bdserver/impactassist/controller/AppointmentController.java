package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.AppointmentDAO;
import com.bdserver.impactassist.model.RequestAppointmentDAO;
import com.bdserver.impactassist.model.UpdateAppointmentStatusDAO;
import com.bdserver.impactassist.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    //TODO make it so that you can only register from today to 30 days after today
    @PostMapping()
    Integer registerAppointment(@RequestBody RequestAppointmentDAO request) {
        return appointmentService.registerAppointment(request);
    }

    //TODO add filtering and sorting capabilities
    @GetMapping
    List<AppointmentDAO> getAllAppointments(@RequestParam(required = false) Integer userId, @RequestParam(required = false) Integer expertId) {
        if (userId != null) {
            return appointmentService.getAppointmentsByUserId(userId);
        }
        if (expertId != null) {
            return appointmentService.getAppointmentsByExpertId(expertId);
        }
        return appointmentService.getAllAppointmentsAuthenticated();
    }

    @GetMapping("/{id}")
    AppointmentDAO getAppointmentsByUserId(@PathVariable int id) {
        return appointmentService.getAppointmentById(id);
    }

    //TODO make it so that only expert can do this
    @PutMapping
    void updateAppointment(@RequestBody UpdateAppointmentStatusDAO updateAppointmentStatusDAO) {
        appointmentService.updateAppointmentStatus(updateAppointmentStatusDAO);
    }
}
