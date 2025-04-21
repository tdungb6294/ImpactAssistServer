package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Mapper
public interface AppointmentRepo {

    @Select("INSERT INTO appointments(user_id, availability_id, title, description, appointment_time, appointment_status) " +
            " VALUES (#{userId}, #{expertAvailabilityId}, #{title}, #{description}, #{appointmentDate}, 'PENDING') RETURNING id")
    int createAvailability(RequestAppointmentDAO requestAppointmentDAO);

    @Update("UPDATE appointments SET appointment_status = #{appointmentStatus} WHERE id = #{appointmentId}")
    void updateAvailabilityStatus(UpdateAppointmentStatusDAO updateAppointmentStatusDAO);

    @Select("SELECT a.id, a.user_id as userId, a.availability_id as availabilityId, a.title, a.description, a.appointment_time as date, a.appointment_status as appointmentStatus, ea.start_time as startTime, ea.end_time as endTime, ea.day_of_week as dayOfWeek, le.longitude as longitude, le.latitude as latitude FROM appointments a LEFT JOIN expert_availabilities ea ON a.availability_id=ea.id LEFT JOIN local_experts le ON ea.local_expert_id=le.user_id WHERE a.user_id = #{userId}" +
            " ORDER BY appointment_time, CASE appointment_status WHEN 'PENDING' THEN 1 WHEN 'CONFIRMED' THEN 2 ELSE 3 END LIMIT #{limit} OFFSET #{offset}")
    List<AppointmentDAO> getAllAppointmentsByUserId(int userId, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @SelectProvider(value = AppointmentSqlProvider.class, method = "getFilteredAppointments")
//    @Select("SELECT a.id as id, a.user_id as userId, a.availability_id as availabilityId, a.title as title" +
//            ", a.description as description, a.appointment_time as date, a.appointment_status as appointmentStatus FROM appointments a JOIN expert_availabilities ea ON a.availability_id=ea.id WHERE ea.local_expert_id = #{expertId}" +
//            " ORDER BY appointment_time, CASE appointment_status WHEN 'PENDING' THEN 1 WHEN 'CONFIRMED' THEN 2 ELSE 3 END")
//    List<AppointmentDAO> getAllAppointmentsByExpertId(int expertId);
    List<AppointmentDAO> getFilteredAppointments(@Param("appointmentStatus") List<AppointmentStatus> appointmentStatus, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("expertId") int expertId, @Param("date") List<LocalDate> date);

    @SelectProvider(value = AppointmentSqlProvider.class, method = "getFilteredAppointmentsCount")
    Integer getFilteredAppointmentsCount(@Param("appointmentStatus") List<AppointmentStatus> appointmentStatus, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("expertId") int expertId, @Param("date") List<LocalDate> date);

    @Select("SELECT a.id, a.user_id as userId, a.availability_id as availabilityId, a.title, a.description, a.appointment_time as date, a.appointment_status as appointmentStatus, ea.start_time as startTime, ea.end_time as endTime, ea.day_of_week as dayOfWeek, le.longitude as longitude, le.latitude as latitude, u.full_name as fullName FROM appointments a LEFT JOIN expert_availabilities ea ON a.availability_id=ea.id LEFT JOIN local_experts le ON ea.local_expert_id=le.user_id LEFT JOIN users u ON u.id=le.user_id WHERE a.id = #{appointmentId}")
    FullAppointmentDAO getAppointmentById(int appointmentId);

    @Select("SELECT COUNT(*) FROM appointments WHERE user_id = #{userId}")
    int getAppointmentCountByUserId(int userId);
}