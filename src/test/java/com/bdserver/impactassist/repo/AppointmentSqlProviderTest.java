package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.AppointmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppointmentSqlProviderTest {

    private final AppointmentSqlProvider provider = new AppointmentSqlProvider();

    @Test
    void testGetFilteredAppointments_basic() {
        String sql = provider.getFilteredAppointments(
                null,
                10,
                0,
                42,
                null
        );

        assertTrue(sql.contains("FROM appointments a"));
        assertTrue(sql.contains("JOIN expert_availabilities ea ON a.availability_id=ea.id"));
        assertTrue(sql.contains("WHERE (ea.local_expert_id = #{expertId})"));
        assertTrue(sql.contains("ORDER BY appointment_time ASC"));
        assertTrue(sql.contains("LIMIT #{limit} OFFSET #{offset}"));
    }

    @Test
    void testGetFilteredAppointments_withStatus() {
        String sql = provider.getFilteredAppointments(
                List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED),
                10,
                0,
                42,
                null
        );

        assertTrue(sql.contains("appointment_status IN ('PENDING','CONFIRMED')"));
    }

    @Test
    void testGetFilteredAppointments_withSingleDate() {
        LocalDate date = LocalDate.of(2024, 5, 1);

        String sql = provider.getFilteredAppointments(
                null,
                10,
                0,
                42,
                List.of(date)
        );

        assertTrue(sql.contains("appointment_time = '" + date.toString() + "'"));
    }

    @Test
    void testGetFilteredAppointments_withDateRange() {
        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate endDate = LocalDate.of(2024, 5, 15);

        String sql = provider.getFilteredAppointments(
                null,
                10,
                0,
                42,
                Arrays.asList(startDate, endDate)
        );

        assertTrue(sql.contains("appointment_time BETWEEN '" + startDate.toString() + "' AND '" + endDate.toString() + "'"));
    }

    @Test
    void testGetFilteredAppointmentsCount_basic() {
        String sql = provider.getFilteredAppointmentsCount(
                null,
                10,
                0,
                42,
                null
        );

        assertTrue(sql.contains("SELECT COUNT(*)"));
        assertTrue(sql.contains("FROM appointments a"));
        assertTrue(sql.contains("JOIN expert_availabilities ea ON a.availability_id=ea.id"));
        assertTrue(sql.contains("WHERE (ea.local_expert_id = #{expertId})"));
    }

    @Test
    void testGetFilteredAppointmentsCount_withStatusAndDate() {
        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate endDate = LocalDate.of(2024, 5, 15);

        String sql = provider.getFilteredAppointmentsCount(
                List.of(AppointmentStatus.CANCELLED),
                10,
                0,
                42,
                Arrays.asList(startDate, endDate)
        );

        assertTrue(sql.contains("appointment_status IN ('CANCELLED')"));
        assertTrue(sql.contains("appointment_time BETWEEN '" + startDate.toString() + "' AND '" + endDate.toString() + "'"));
    }
}