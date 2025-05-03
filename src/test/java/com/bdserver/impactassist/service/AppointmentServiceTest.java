package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.AppointmentRepo;
import com.bdserver.impactassist.repo.LocalExpertAvailabilityRepo;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {


    static LocalDate now = LocalDate.now();
    static RequestAppointmentDAO requestAppointmentDAO;
    static FullAppointmentDAO fullAppointmentDAO;
    static UpdateAppointmentStatusDAO updateAppointmentStatusDAO;

    @Mock
    private UserService userService;
    @Mock
    private AppointmentRepo appointmentRepo;
    @Mock
    private LocalExpertAvailabilityRepo localExpertAvailabilityRepo;
    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeAll
    static void setUp() {
        requestAppointmentDAO = RequestAppointmentDAO.builder()
                .expertAvailabilityId(1)
                .appointmentDate(now.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)))
                .title("Check my car damage")
                .description("Check my car damage")
                .build();
        fullAppointmentDAO = FullAppointmentDAO.builder()
                .id(1)
                .title("Check my car damage")
                .description("Check my car damage")
                .date(now)
                .availabilityId(1)
                .userId(1)
                .appointmentStatus(AppointmentStatus.COMPLETED)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .dayOfWeek(DayOfWeekEnum.Monday)
                .latitude(1)
                .longitude(1)
                .fullName("John")
                .build();
        updateAppointmentStatusDAO = UpdateAppointmentStatusDAO.builder()
                .appointmentId(1)
                .appointmentStatus(AppointmentStatus.COMPLETED)
                .build();
    }

    @Test
    public void registerAppointment() throws BadRequestException {
        when(localExpertAvailabilityRepo.getAvailabilityDayOfWeekById(anyInt())).thenReturn(String.valueOf(DayOfWeek.MONDAY));
        assertThrows(BadRequestException.class, () -> {
            appointmentService.registerAppointment(requestAppointmentDAO);
        });
    }


    @Test
    public void getAppointmentByExpertId() {
        when(userService.getUserId()).thenReturn(1);
        when(appointmentRepo.getFilteredAppointments(anyList(), anyInt(), anyInt(), anyInt(), anyList())).thenReturn(List.of());
        when(appointmentRepo.getFilteredAppointmentsCount(anyList(), anyInt(), anyInt(), anyInt(), anyList())).thenReturn(1);
        Map<String, Object> result = appointmentService.getAppointmentsByExpertId(List.of(AppointmentStatus.PENDING), List.of(now, now.plusDays(4)), 1, 5);
        assertEquals(List.of(), result.get("appointments"));
        assertEquals(1, result.get("currentPage"));
        assertEquals(1, result.get("totalPages"));
        assertEquals(1, result.get("total"));
        assertNull(result.get("nextPage"));
    }

    @Test
    public void getAppointmentsByUserId() {
        when(appointmentRepo.getAllAppointmentsByUserId(anyInt(), anyInt(), anyInt())).thenReturn(List.of());
        when(appointmentRepo.getAppointmentCountByUserId(anyInt())).thenReturn(1);
        Map<String, Object> result = appointmentService.getAppointmentsByUserId(1, 1, 5);
        assertEquals(List.of(), result.get("appointments"));
        assertEquals(1, result.get("currentPage"));
        assertEquals(1, result.get("totalPages"));
        assertEquals(1, result.get("total"));
        assertNull(result.get("nextPage"));
    }

    @Test
    public void getAppointmentById() {
        when(appointmentRepo.getAppointmentById(1)).thenReturn(fullAppointmentDAO);
        assertEquals(fullAppointmentDAO, appointmentService.getAppointmentById(1));
    }

    @Test
    public void updateAppointmentStatus() throws BadRequestException {
        when(userService.getUserId()).thenReturn(1);
        when(localExpertAvailabilityRepo.getAvailabilitiesByUserId(anyInt(), anyInt())).thenReturn(true);
        doNothing().when(appointmentRepo).updateAvailabilityStatus(updateAppointmentStatusDAO);
        appointmentService.updateAppointmentStatus(updateAppointmentStatusDAO);
        when(localExpertAvailabilityRepo.getAvailabilitiesByUserId(anyInt(), anyInt())).thenReturn(false);
        assertThrows(BadRequestException.class, () -> {
            appointmentService.updateAppointmentStatus(updateAppointmentStatusDAO);
        });
    }
}