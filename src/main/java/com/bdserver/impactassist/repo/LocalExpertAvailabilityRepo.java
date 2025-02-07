package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.AvailabilityDAO;
import com.bdserver.impactassist.model.RequestAvailabilityDAO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LocalExpertAvailabilityRepo {
    @Select("SELECT * FROM expert_availabilities WHERE local_expert_id = #{localExpertId}")
    List<AvailabilityDAO> getAvailabilitiesById(int localExpertId);

    @Insert("INSERT INTO expert_availabilities (start_time, end_time, day_of_week, local_expert_id) VALUES (#{startTime}, #{endTime}, #{dayOfWeek}, #{localExpertId})")
    void createAvailability(RequestAvailabilityDAO requestAvailabilityDAO);

    @Delete("DELETE FROM expert_availabilities WHERE id = #{id}")
    void deleteAvailability(int id);
}