package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.AvailabilityDAO;
import com.bdserver.impactassist.model.RequestAvailabilityDAO;
import com.bdserver.impactassist.model.UnavailabilityDAO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LocalExpertAvailabilityRepo {
    @Select("SELECT id, local_expert_id AS localExpertId, start_time AS startTime, end_time AS endTime, day_of_week AS dayOfWeek FROM expert_availabilities WHERE local_expert_id = #{localExpertId}")
    List<AvailabilityDAO> getAvailabilitiesById(int localExpertId);

    @Insert("INSERT INTO expert_availabilities (start_time, end_time, day_of_week, local_expert_id) VALUES (#{startTime}, #{endTime}, #{dayOfWeek}, #{localExpertId})")
    void createAvailability(RequestAvailabilityDAO requestAvailabilityDAO);

    @Delete("DELETE FROM expert_availabilities WHERE id = #{id}")
    void deleteAvailability(int id);

    @Select("SELECT day_of_week FROM expert_availabilities WHERE id = #{id}")
    String getAvailabilityDayOfWeekById(int id);

    @Select("SELECT a.appointment_time AS date, a.availability_id as availabilityId FROM appointments a JOIN expert_availabilities ea ON a.availability_id=ea.id WHERE ea.local_expert_id = #{expertId} AND a.appointment_time > CURRENT_DATE")
    List<UnavailabilityDAO> getUnavailableAppointments(int expertId);
}