package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.AppointmentDAO;
import com.bdserver.impactassist.model.RequestAppointmentDAO;
import com.bdserver.impactassist.model.UpdateAppointmentStatusDAO;
import com.bdserver.impactassist.repo.AppointmentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final UserService userService;

    public AppointmentService(AppointmentRepo appointmentRepo, UserService userService) {
        this.appointmentRepo = appointmentRepo;
        this.userService = userService;
    }

    public Integer registerAppointment(RequestAppointmentDAO request) {
        request.setUserId(userService.getUserId());
        return appointmentRepo.createAvailability(request);
    }

    public List<AppointmentDAO> getAppointmentsByExpertId(int expertId) {
        return appointmentRepo.getAllAppointmentsByExpertId(expertId);
    }

    public List<AppointmentDAO> getAppointmentsByUserId(int userId) {
        return appointmentRepo.getAllAppointmentsByUserId(userId);
    }

    public List<AppointmentDAO> getAllAppointmentsAuthenticated() {
        return appointmentRepo.getAllAppointmentsByUserId(userService.getUserId());
    }

    public AppointmentDAO getAppointmentById(int id) {
        return appointmentRepo.getAppointmentById(id);
    }

    public void updateAppointmentStatus(UpdateAppointmentStatusDAO update) {
        appointmentRepo.updateAvailabilityStatus(update);
    }
}
