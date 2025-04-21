package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.AutoPartsAndServicesDAO;
import com.bdserver.impactassist.model.DamageReportDAO;
import com.bdserver.impactassist.model.PartialDamageReportDAO;
import com.bdserver.impactassist.model.RequestDamageReportDAO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DamageReportRepo {

    @Insert("INSERT INTO damage_reports (user_id, claim_id) VALUES(#{userId}, #{claimId})")
    @Options(useGeneratedKeys = false, keyProperty = "report_id", keyColumn = "report_id")
    Integer createReport(int userId, int claimId);

    @Insert("<script>" +
            "INSERT INTO damage_report_data (report_id, auto_part_service_id) VALUES " +
            "<foreach collection='request.autoPartsAndServices' item='autoPartId' separator=','>" +
            "(#{request.reportId}, #{autoPartId})" +
            "</foreach>" +
            "</script>")
    void addReportDataToReport(@Param("request") RequestDamageReportDAO request);

    @Select("SELECT dr.report_id as reportId, u.full_name as fullName FROM damage_reports dr JOIN users u ON dr.user_id = u.id")
    List<PartialDamageReportDAO> getReportList(Integer claimId);

    @Select("SELECT apas.id, apas.auto_part as autoPart, apas.description, apas.min_price as minPrice, apas.max_price as maxPrice, apasc.category_name as categoryName FROM damage_report_data drd JOIN auto_parts_and_services apas ON drd.auto_part_service_id = apas.id JOIN public.auto_parts_and_services_categories apasc on apas.category_id = apasc.id WHERE report_id = #{reportId}")
    List<AutoPartsAndServicesDAO> getAutoPartsAndServicesByReportId(Integer reportId);

    @Select("SELECT drs.report_id as reportId, " +
            "drs.full_name as fullName, " +
            "drs.estimated_min_price_without_service as estimatedMinPriceWithoutService, " +
            "drs.estimated_min_price_with_service as estimatedMinPriceWithService, " +
            "drs.estimated_max_price_without_service as estimatedMaxPriceWithoutService, " +
            "drs.estimated_max_price_with_service as estimatedMaxPriceWithService " +
            "FROM damage_report_summary drs WHERE drs.report_id = #{reportId};")
    DamageReportDAO getDamageReport(Integer reportId);
}
