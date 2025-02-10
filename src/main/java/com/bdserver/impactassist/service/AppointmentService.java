package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.AppointmentDAO;
import com.bdserver.impactassist.model.AppointmentStatusEnum;
import com.bdserver.impactassist.model.RequestAppointmentDAO;
import com.bdserver.impactassist.model.UpdateAppointmentStatusDAO;
import com.bdserver.impactassist.repo.AppointmentRepo;
import com.bdserver.impactassist.repo.LocalExpertAvailabilityRepo;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final UserService userService;
    private final LocalExpertAvailabilityRepo localExpertAvailabilityRepo;

    public AppointmentService(AppointmentRepo appointmentRepo, UserService userService, LocalExpertAvailabilityRepo localExpertAvailabilityRepo) {
        this.appointmentRepo = appointmentRepo;
        this.userService = userService;
        this.localExpertAvailabilityRepo = localExpertAvailabilityRepo;
    }

    public Integer registerAppointment(RequestAppointmentDAO request) throws BadRequestException {
        String appointmentDateDayOfWeek = request.getAppointmentDate().getDayOfWeek().toString();
        String dayOfWeek = localExpertAvailabilityRepo.getAvailabilityDayOfWeekById(request.getExpertAvailabilityId()).toUpperCase();
        if (!appointmentDateDayOfWeek.equals(dayOfWeek)) {
            throw new BadRequestException("Invalid day of week");
        }
        request.setUserId(userService.getUserId());
        return appointmentRepo.createAvailability(request);
    }

    public List<AppointmentDAO> getAppointmentsByExpertId(List<AppointmentStatusEnum> appointmentStatus, Integer limit, Integer offset, int expertId, List<LocalDate> date) {
        return appointmentRepo.getFilteredAppointments(appointmentStatus, limit, offset, expertId, date);
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
