package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ClaimRepo {

    @Select("INSERT INTO claims (user_id, claim_type, claim_status) VALUES (#{userId}, #{claimType}, #{claimStatus}) RETURNING id")
    Integer createNewClaim(ClaimDAO claimDAO);

    @Insert("INSERT INTO car_claims (id, car_model, vehicle_registration_number, vehicle_identification_number," +
            "odometer_mileage, insurance_policy_number, insurance_company, accident_datetime, location_longitude," +
            "location_latitude, address, description, police_involved, police_report_number, weather_condition," +
            "compensation_method, additional_notes, data_management_consent, international_bank_account_number) VALUES (" +
            "#{id}, #{carModel}, #{vehicleRegistrationNumber}, #{vehicleIdentificationNumber}, " +
            "#{odometerMileage}, #{insurancePolicyNumber}, #{insuranceCompany}, #{accidentDatetime}, #{locationLongitude}," +
            "#{locationLatitude}, #{address}, #{description}, #{policeInvolved}, #{policeReportNumber}, #{weatherCondition}," +
            "#{compensationMethod}, #{additionalNotes}, #{dataManagementConsent}, #{internationalBankAccountNumber})")
    void createNewCarClaim(RegisterCarClaimDAO registerCarClaimDAO);

    @Insert("INSERT INTO claims_accident_images (claim_id, file_name, unique_file_identifier, is_deleted) " +
            "VALUES(#{claimId}, #{claimImageDAO.fileName}, #{claimImageDAO.uniqueFileIdentifier}, false)")
    void addClaimImage(ClaimImageDAO claimImageDAO, Integer claimId);

    @Insert("INSERT INTO claims_accident_documents (claim_id, file_name, unique_file_identifier, is_deleted, document_type) " +
            "VALUES(#{claimId}, #{claimDocumentDAO.fileName}, #{claimDocumentDAO.uniqueFileIdentifier}, false, #{claimDocumentDAO.documentType})")
    void addClaimDocument(ClaimDocumentDAO claimDocumentDAO, Integer claimId);

    @Select("SELECT cc.id, cc.car_model as carModel, cc.accident_datetime as accidentDatetime, cc.address FROM car_claims cc LEFT JOIN claims c ON cc.id = c.id WHERE c.user_id = #{userId}")
    List<PartialCarClaimDAO> getCarClaimsByUserId(int userId);

    @Select("SELECT cc.id, cc.car_model as carModel, cc.accident_datetime as accidentDatetime, cc.address, c.claim_status as claimStatus FROM car_claims cc LEFT JOIN claims c ON cc.id = c.id WHERE c.user_id = #{userId} ORDER BY c.created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<PartialCarClaimDAO> getPagedCarClaimsByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM car_claims cc LEFT JOIN claims c ON cc.id = c.id WHERE c.user_id = #{userId}")
    int getCarClaimsCount(int userId);

    @Select("SELECT cc.id as id, cc.car_model as carModel, c.user_id as userId, cc.vehicle_registration_number as vehicleRegistrationNumber, cc.vehicle_identification_number as vehicleIdentificationNumber, " +
            "cc.odometer_mileage as odometerMileage, cc.insurance_policy_number as insurancePolicyNumber, cc.insurance_company AS insuranceCompany, " +
            "cc.accident_datetime as accidentDatetime, cc.location_longitude as locationLongitude, cc.location_latitude AS locationLatitude, " +
            "cc.address as address, cc.description as description, cc.police_involved as policeInvolved, cc.police_report_number as policeReportNumber," +
            "cc.weather_condition as weatherCondition, cc.compensation_method as compensationMethod, cc.additional_notes as additionalNotes," +
            "cc.data_management_consent as dataManagementConsent, cc.international_bank_account_number as internationalBankAccountNumber," +
            "cc.created_at as createdAt, cc.updated_at as updatedAt, c.claim_status as claimStatus FROM car_claims cc LEFT JOIN claims c ON cc.id = c.id WHERE c.id = #{id}")
    CarClaimDAO getCarClaimById(int id);

    @Select("SELECT CONCAT(unique_file_identifier, '_', file_name) AS url, document_type as documentType FROM claims_accident_documents WHERE claim_id = #{claimId}")
    List<PartialClaimDocumentDAO> getClaimAccidentDocumentNames(int claimId);

    @Select("SELECT CONCAT(unique_file_identifier, '_', file_name) FROM claims_accident_images WHERE claim_id = #{claimId}")
    List<String> getClaimAccidentImageNames(int claimId);

    @Select("SELECT cc.id as id, c.user_id as userId, cc.vehicle_registration_number as vehicleRegistrationNumber, cc.vehicle_identification_number as vehicleIdentificationNumber, " +
            "cc.odometer_mileage as odometerMileage, cc.insurance_policy_number as insurancePolicyNumber, cc.insurance_company AS insuranceCompany, " +
            "cc.accident_datetime as accidentDatetime, cc.location_longitude as locationLongitude, cc.location_latitude AS locationLatitude, " +
            "cc.address as address, cc.description as description, cc.police_involved as policeInvolved, cc.police_report_number as policeReportNumber," +
            "cc.weather_condition as weatherCondition, cc.compensation_method as compensationMethod, cc.additional_notes as additionalNotes," +
            "cc.data_management_consent as dataManagementConsent, cc.international_bank_account_number as internationalBankAccountNumber," +
            "cc.created_at as createdAt, cc.updated_at as updatedAt FROM car_claims cc LEFT JOIN claims c ON cc.id = c.id WHERE c.id = #{id}")
    CarClaimMultipartDAO getCarClaimDetailsById(int claimId);

    @Select("SELECT cc.id as id, cc.object_type as objectType, cc.object_material as objectMaterial, cc.object_ownership as objectOwnership, cc.damage_to_object_description as damageToObjectDescription, cc.insurance_policy_number as insurancePolicyNumber, cc.insurance_company AS insuranceCompany, " +
            "cc.accident_datetime as accidentDatetime, cc.location_longitude as locationLongitude, cc.location_latitude AS locationLatitude, " +
            "cc.address as address, cc.description as description, cc.police_involved as policeInvolved, cc.police_report_number as policeReportNumber," +
            "cc.weather_condition as weatherCondition, cc.compensation_method as compensationMethod, cc.additional_notes as additionalNotes," +
            "cc.data_management_consent as dataManagementConsent, cc.international_bank_account_number as internationalBankAccountNumber," +
            "cc.created_at as createdAt, cc.updated_at as updatedAt, c.claim_status as claimStatus FROM object_claims cc LEFT JOIN claims c ON cc.id = c.id WHERE c.id = #{id}")
    ObjectClaimDAO getObjectClaimById(Integer claimId);

    @Insert("INSERT INTO object_claims (id, object_type, object_material, object_ownership," +
            "damage_to_object_description, insurance_policy_number, insurance_company, accident_datetime, location_longitude," +
            "location_latitude, address, description, police_involved, police_report_number, weather_condition," +
            "compensation_method, additional_notes, data_management_consent, international_bank_account_number) VALUES (" +
            "#{id}, #{objectType}, #{objectMaterial}, #{objectOwnership}, " +
            "#{damageToObjectDescription}, #{insurancePolicyNumber}, #{insuranceCompany}, #{accidentDatetime}, #{locationLongitude}," +
            "#{locationLatitude}, #{address}, #{description}, #{policeInvolved}, #{policeReportNumber}, #{weatherCondition}," +
            "#{compensationMethod}, #{additionalNotes}, #{dataManagementConsent}, #{internationalBankAccountNumber})")
    void createNewObjectClaim(RegisterObjectClaimDAO registerObjectClaimDAO);

    @SelectProvider(value = ClaimSqlProvider.class, method = "getFilteredPagedClaimsByLocalExpertId")
    List<PartialClaimDAO> getFilteredPagedClaimsByLocalExpertId(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") List<ClaimStatus> status);

    @SelectProvider(value = ClaimSqlProvider.class, method = "getFilteredPagedClaimCountByLocalExpertId")
    int getFilteredPagedClaimCountByLocalExpertId(int userId, @Param("status") List<ClaimStatus> status);

    @Update("UPDATE claims SET shared_id = #{localExpertId} WHERE id = #{claimId}")
    void shareClaim(RequestShareClaimDAO share);

    @SelectProvider(value = ClaimSqlProvider.class, method = "getFilteredPagedClaimsByUserId")
    List<PartialClaimDAO> getFilteredPagedClaimsByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") List<ClaimStatus> status);

    @SelectProvider(value = ClaimSqlProvider.class, method = "getFilteredPagedClaimCountByUserId")
    int getFilteredPagedClaimCountByUserId(@Param("userId") int userId, @Param("status") List<ClaimStatus> status);
}
