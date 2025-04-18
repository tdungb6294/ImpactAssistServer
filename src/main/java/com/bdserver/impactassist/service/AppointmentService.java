package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.AppointmentRepo;
import com.bdserver.impactassist.repo.LocalExpertAvailabilityRepo;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PreAuthorize("hasAuthority('LOCAL_EXPERT')")
    public Map<String, Object> getAppointmentsByExpertId(List<AppointmentStatusEnum> appointmentStatus, List<LocalDate> date, int page, int size) {
        int expertId = userService.getUserId();
        int offset = (page - 1) * size;
        List<AppointmentDAO> appointments = appointmentRepo.getFilteredAppointments(appointmentStatus, size, offset, expertId, date);
        Integer count = appointmentRepo.getFilteredAppointmentsCount(appointmentStatus, size, offset, expertId, date);
        int total = count != null ? count : 0;
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("appointments", appointments);
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public Map<String, Object> getAppointmentsByUserId(int userId, int page, int size) {
        int offset = (page - 1) * size;
        int total = appointmentRepo.getAppointmentCountByUserId(userId);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("appointments", appointmentRepo.getAllAppointmentsByUserId(userId, size, offset));
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public Map<String, Object> getAllAppointmentsAuthenticated(int page, int size) {
        int offset = (page - 1) * size;
        int userId = userService.getUserId();
        int total = appointmentRepo.getAppointmentCountByUserId(userId);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("appointments", appointmentRepo.getAllAppointmentsByUserId(userId, size, offset));
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public FullAppointmentDAO getAppointmentById(int id) {
        return appointmentRepo.getAppointmentById(id);
    }

    public void updateAppointmentStatus(UpdateAppointmentStatusDAO update) throws BadRequestException {
        Boolean isBelongingToExpert = isBelongingToExpert(update.getAppointmentId());
        if (isBelongingToExpert != null && isBelongingToExpert) {
            appointmentRepo.updateAvailabilityStatus(update);
        } else {
            throw new BadRequestException();
        }
    }

    public Boolean isBelongingToExpert(int availabilityId) {
        int userId = userService.getUserId();
        return localExpertAvailabilityRepo.getAvailabilitiesByUserId(userId, availabilityId);
    }
}
