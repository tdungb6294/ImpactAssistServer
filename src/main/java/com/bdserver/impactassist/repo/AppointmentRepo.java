package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.AppointmentDAO;
import com.bdserver.impactassist.model.RequestAppointmentDAO;
import com.bdserver.impactassist.model.UpdateAppointmentStatusDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AppointmentRepo {

    @Select("INSERT INTO appointments(user_id, availability_id, title, description, appointment_time, appointment_status) " +
            " VALUES (#{userId}, #{expertAvailabilityId}, #{title}, #{description}, #{appointmentDate}, 'PENDING') RETURNING id")
    int createAvailability(RequestAppointmentDAO requestAppointmentDAO);

    @Update("UPDATE appointments SET appointment_status = #{appointmentStatus} WHERE id = #{appointmentId}")
    void updateAvailabilityStatus(UpdateAppointmentStatusDAO updateAppointmentStatusDAO);

    @Select("SELECT id, user_id as userId, availability_id as availabilityId, title, description, appointment_time as date, appointment_status as appointmentStatus FROM appointments WHERE user_id = #{userId}" +
            " ORDER BY appointment_time, CASE appointment_status WHEN 'PENDING' THEN 1 WHEN 'CONFIRMED' THEN 2 ELSE 4 END")
    List<AppointmentDAO> getAllAppointmentsByUserId(int userId);

    @Select("SELECT a.id as id, a.user_id as userId, a.availability_id as availabilityId, a.title as title" +
            ", a.description as description, a.appointment_time as date, a.appointment_status as appointmentStatus FROM appointments a JOIN expert_availabilities ea ON a.availability_id=ea.id WHERE ea.local_expert_id = #{expertId}" +
            " ORDER BY appointment_time, CASE appointment_status WHEN 'PENDING' THEN 1 WHEN 'CONFIRMED' THEN 2 ELSE 4 END")
    List<AppointmentDAO> getAllAppointmentsByExpertId(int expertId);

    @Select("SELECT id, user_id as userId, availability_id as availabilityId, title, description, appointment_time as date, appointment_status as appointmentStatus FROM appointments WHERE id = #{appointmentId}")
    AppointmentDAO getAppointmentById(int appointmentId);
}