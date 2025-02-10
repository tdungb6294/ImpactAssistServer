package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ClaimRepo {
    @Select("INSERT INTO claims (user_id, car_model, vehicle_registration_number, vehicle_identification_number," +
            "odometer_mileage, insurance_policy_number, insurance_company, accident_datetime, location_longitude," +
            "location_latitude, address, description, police_involved, police_report_number, weather_condition," +
            "compensation_method, additional_notes, data_management_consent, international_bank_account_number) VALUES (" +
            "#{userId}, #{carModel}, #{vehicleRegistrationNumber}, #{vehicleIdentificationNumber}, " +
            "#{odometerMileage}, #{insurancePolicyNumber}, #{insuranceCompany}, #{accidentDatetime}, #{locationLongitude}," +
            "#{locationLatitude}, #{address}, #{description}, #{policeInvolved}, #{policeReportNumber}, #{weatherCondition}," +
            "#{compensationMethod}, #{additionalNotes}, #{dataManagementConsent}, #{internationalBankAccountNumber}) " +
            "RETURNING id")
    Integer createNewClaim(RegisterClaimDAO registerClaimDAO);

    @Insert("INSERT INTO claims_accident_images (claim_id, file_name, unique_file_identifier, is_deleted) " +
            "VALUES(#{claimId}, #{claimImageDAO.fileName}, #{claimImageDAO.uniqueFileIdentifier}, false)")
    void addClaimImage(ClaimImageDAO claimImageDAO, Integer claimId);

    @Insert("INSERT INTO claims_accident_documents (claim_id, file_name, unique_file_identifier, is_deleted, document_type) " +
            "VALUES(#{claimId}, #{claimDocumentDAO.fileName}, #{claimDocumentDAO.uniqueFileIdentifier}, false, #{claimDocumentDAO.documentType})")
    void addClaimDocument(ClaimDocumentDAO claimDocumentDAO, Integer claimId);

    @Select("SELECT id, car_model as carModel, accident_datetime as accidentDatetime, address FROM claims WHERE user_id = #{userId}")
    List<PartialClaimDAO> getClaimsByUserId(int userId);

    @Select("SELECT id, user_id as userId, vehicle_registration_number as vehicleRegistrationNumber, vehicle_identification_number as vehicleIdentificationNumber, " +
            "odometer_mileage as odometerMileage, insurance_policy_number as insurancePolicyNumber, insurance_company AS insuranceCompany, " +
            "accident_datetime as accidentDatetime, location_longitude as locationLongitude, location_latitude AS locationLatitude, " +
            "address, description, police_involved as policeInvolved, police_report_number as policeReportNumber," +
            "weather_condition as weatherCondition, compensation_method as compensationMethod, additional_notes as additionalNotes," +
            "data_management_consent as dataManagementConsent, international_bank_account_number as internationalBankAccountNumber," +
            "created_at as createdAt, updated_at as updatedAt FROM claims WHERE id = #{id}")
    ClaimDAO getClaimById(int id);

    @Select("SELECT CONCAT(unique_file_identifier, '_', file_name) AS url, document_type as documentType FROM claims_accident_documents WHERE claim_id = #{claimId}")
    List<PartialClaimDocumentDAO> getClaimAccidentDocumentNames(int claimId);

    @Select("SELECT CONCAT(unique_file_identifier, '_', file_name) FROM claims_accident_images WHERE claim_id = #{claimId}")
    List<String> getClaimAccidentImageNames(int claimId);
}
