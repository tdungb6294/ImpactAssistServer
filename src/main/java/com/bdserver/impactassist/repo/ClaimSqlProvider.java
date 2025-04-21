package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.ClaimStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

public class ClaimSqlProvider {
    public String getFilteredPagedClaimsByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") List<ClaimStatus> status) {
        return new SQL() {{
            SELECT("c.id, " +
                    "cc.car_model as carModel, " +
                    "CASE " +
                    "WHEN oc.accident_datetime IS NOT NULL THEN oc.accident_datetime " +
                    "WHEN cc.accident_datetime IS NOT NULL THEN cc.accident_datetime " +
                    "ELSE NULL END AS accidentDatetime, " +
                    "CASE " +
                    "WHEN oc.address IS NOT NULL THEN oc.address " +
                    "WHEN cc.address IS NOT NULL THEN cc.address " +
                    "ELSE NULL END AS address, " +
                    "c.claim_status as claimStatus, " +
                    "c.claim_type as claimType, " +
                    "oc.object_type as objectType");
            FROM("claims c");
            LEFT_OUTER_JOIN("car_claims cc ON cc.id = c.id");
            LEFT_OUTER_JOIN("object_claims oc ON c.id = oc.id");
            WHERE("c.user_id = #{userId}");
            if (status != null && !status.isEmpty()) {
                String inClause = "c.claim_status IN " + status.stream()
                        .map(s -> "'" + s + "'")
                        .collect(Collectors.joining(", ", "(", ")"));
                WHERE(inClause);
            } else {
                WHERE("c.claim_status != 'REJECTED'");
            }
            ORDER_BY("CASE c.claim_status WHEN 'APPROVED' THEN 1 WHEN 'PENDING' THEN 2 ELSE 3 END");
            ORDER_BY("c.created_at DESC");
            LIMIT("#{limit}");
            OFFSET("#{offset}");
        }}.toString();
    }

    public String getFilteredPagedClaimCountByUserId(@Param("userId") int userId, @Param("status") List<ClaimStatus> status) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("claims c");
            LEFT_OUTER_JOIN("car_claims cc ON cc.id = c.id");
            LEFT_OUTER_JOIN("object_claims oc ON c.id = oc.id");
            WHERE("c.user_id = #{userId}");
            if (status != null && !status.isEmpty()) {
                String inClause = "c.claim_status IN " + status.stream()
                        .map(s -> "'" + s + "'")
                        .collect(Collectors.joining(", ", "(", ")"));
                WHERE(inClause);
            } else {
                WHERE("c.claim_status != 'REJECTED'");
            }
        }}.toString();
    }

    public String getFilteredPagedClaimsByLocalExpertId(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") List<ClaimStatus> status) {
        return new SQL() {{
            SELECT("c.id, " +
                    "cc.car_model as carModel, " +
                    "CASE " +
                    "WHEN oc.accident_datetime IS NOT NULL THEN oc.accident_datetime " +
                    "WHEN cc.accident_datetime IS NOT NULL THEN cc.accident_datetime " +
                    "ELSE NULL END AS accidentDatetime, " +
                    "CASE " +
                    "WHEN oc.address IS NOT NULL THEN oc.address " +
                    "WHEN cc.address IS NOT NULL THEN cc.address " +
                    "ELSE NULL END AS address, " +
                    "c.claim_status as claimStatus, " +
                    "c.claim_type as claimType, " +
                    "oc.object_type as objectType");
            FROM("claims c");
            LEFT_OUTER_JOIN("car_claims cc ON cc.id = c.id");
            LEFT_OUTER_JOIN("object_claims oc ON c.id = oc.id");
            WHERE("c.shared_id = #{userId}");
            if (status != null && !status.isEmpty()) {
                String inClause = "c.claim_status IN " + status.stream()
                        .map(s -> "'" + s + "'")
                        .collect(Collectors.joining(", ", "(", ")"));
                WHERE(inClause);
            } else {
                WHERE("c.claim_status != 'REJECTED'");
            }
            ORDER_BY("CASE c.claim_status WHEN 'APPROVED' THEN 1 WHEN 'PENDING' THEN 2 ELSE 3 END");
            ORDER_BY("c.created_at DESC");
            LIMIT("#{limit}");
            OFFSET("#{offset}");
        }}.toString();
    }

    public String getFilteredPagedClaimCountByLocalExpertId(@Param("userId") int userId, @Param("status") List<ClaimStatus> status) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("claims c");
            LEFT_OUTER_JOIN("car_claims cc ON cc.id = c.id");
            LEFT_OUTER_JOIN("object_claims oc ON c.id = oc.id");
            WHERE("c.shared_id = #{userId}");
            if (status != null && !status.isEmpty()) {
                String inClause = "c.claim_status IN " + status.stream()
                        .map(s -> "'" + s + "'")
                        .collect(Collectors.joining(", ", "(", ")"));
                WHERE(inClause);
            } else {
                WHERE("c.claim_status != 'REJECTED'");
            }
        }}.toString();
    }
}
