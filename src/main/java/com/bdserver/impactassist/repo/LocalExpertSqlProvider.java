package com.bdserver.impactassist.repo;

import org.apache.ibatis.jdbc.SQL;

public class LocalExpertSqlProvider {
    public String getLocalExperts(String search) {
        return new SQL() {{
            SELECT("id, full_name as fullName, email, phone, longitude, latitude, description");
            FROM("local_experts le");
            JOIN("users u ON le.user_id=u.id");
            if (search != null && !search.isEmpty()) {
                WHERE("full_name ILIKE CONCAT('%', #{search}, '%')");
            }
        }}.toString();
    }
}
