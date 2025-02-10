package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.AppointmentStatusEnum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentSqlProvider {
    public String getFilteredAppointments(@Param("appointmentStatus") List<AppointmentStatusEnum> appointmentStatus, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("expertId") int expertId, @Param("date") List<LocalDate> date) {
        return new SQL() {
            {
                SELECT("a.id as id, " +
                        "a.user_id as userId, " +
                        "a.availability_id as availabilityId, " +
                        "a.title as title, " +
                        "a.description as description, " +
                        "a.appointment_time as date, " +
                        "a.appointment_status as appointmentStatus");
                FROM("appointments a");
                JOIN("expert_availabilities ea ON a.availability_id=ea.id");
                WHERE("ea.local_expert_id = #{expertId}");
                if (appointmentStatus != null && !appointmentStatus.isEmpty()) {
                    WHERE("appointment_status IN (" + appointmentStatus.stream().map(status -> "'" + status.name() + "'").collect(Collectors.joining(",")) + ")");
                }
                if (date != null && !date.isEmpty()) {
                    if (date.size() == 1) {
                        WHERE("appointment_time = '" + date.getFirst().toString() + "'");
                    } else {
                        WHERE("appointment_time BETWEEN '" + date.getFirst().toString() + "' AND '" + date.get(1).toString() + "'");
                    }
                }
                ORDER_BY("appointment_time ASC");
                ORDER_BY("CASE appointment_status WHEN 'PENDING' THEN 1 WHEN 'CONFIRMED' THEN 2 ELSE 3 END");
            }
        } + " LIMIT #{limit} OFFSET #{offset}";
    }
}