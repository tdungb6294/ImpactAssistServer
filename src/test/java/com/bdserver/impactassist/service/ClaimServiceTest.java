package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.RegisterCarClaimDAO;
import com.bdserver.impactassist.model.RegisterObjectClaimDAO;
import com.bdserver.impactassist.repo.ClaimRepo;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {
    static RegisterCarClaimDAO registerCarClaimDAO;
    static RegisterObjectClaimDAO registerObjectClaimDAO;
    static List<MultipartFile> files;
    static LocalDateTime now = LocalDateTime.now();

    @Mock
    private UserService userService;
    @Mock
    private S3Service s3Service;
    @Mock
    private ClaimRepo claimRepo;
    @InjectMocks
    private ClaimService claimService;

    @BeforeAll
    static void setUp() {
        files = List.of(mock(MultipartFile.class), mock(MultipartFile.class));

        registerCarClaimDAO = RegisterCarClaimDAO.builder()
                .carModel("Car Model")
                .vehicleRegistrationNumber("Vehicle Registration Number")
                .vehicleIdentificationNumber("Vehicle Identification Number")
                .odometerMileage("Odometer Mileage")
                .insurancePolicyNumber("Insurance Policy Number")
                .insuranceCompany("Insurance Company")
                .accidentDatetime(now)
                .locationLatitude(1)
                .locationLongitude(1)
                .address("Address")
                .description("Description")
                .policeInvolved(true)
                .policeReportNumber(null)
                .weatherCondition("Weather Condition")
                .compensationMethod("Compensation Method")
                .additionalNotes("Additional Notes")
                .dataManagementConsent(true)
                .internationalBankAccountNumber("International Bank Account Number")
                .documentTypes(List.of("Document"))
                .build();

        registerObjectClaimDAO = RegisterObjectClaimDAO.builder()
                .objectOwnership("Object")
                .objectType("Object")
                .objectMaterial("Object")
                .damageToObjectDescription("Damage")
                .insurancePolicyNumber("Insurance Policy Number")
                .insuranceCompany("Insurance Company")
                .accidentDatetime(now)
                .locationLatitude(1)
                .locationLongitude(1)
                .address("Address")
                .description("Description")
                .policeInvolved(true)
                .policeReportNumber(null)
                .weatherCondition("Weather Condition")
                .compensationMethod("Compensation Method")
                .additionalNotes("Additional Notes")
                .dataManagementConsent(true)
                .internationalBankAccountNumber("International Bank Account Number")
                .documentTypes(List.of("Document"))
                .build();
    }

    @Test
    void registerCarClaim() throws IOException {
        assertThrows(BadRequestException.class, () -> {
            claimService.registerCarClaim(files, files, registerCarClaimDAO);
        });
        when(files.getFirst().getOriginalFilename()).thenReturn("file1.txt");
        when(files.get(1).getOriginalFilename()).thenReturn("file2.txt");
        registerCarClaimDAO.setDocumentTypes(List.of("Document", "Document"));
        doThrow(IOException.class).when(s3Service).uploadFile(any(), any());
        assertThrows(IOException.class, () -> {
            claimService.registerCarClaim(files, files, registerCarClaimDAO);
        });
    }

    @Test
    void getCarClaim() {
        when(claimRepo.getCarClaimById(anyInt())).thenReturn(null);
        assertThrows(BadRequestException.class, () -> {
            claimService.getCarClaim(anyInt());
        });
    }

    @Test
    void getClaimDetails() {
        when(claimRepo.getCarClaimDetailsById(anyInt())).thenReturn(null);
        assertThrows(BadRequestException.class, () -> {
            claimService.getClaimDetails(anyInt());
        });
    }

    @Test
    void getObjectClaim() {
        when(claimRepo.getObjectClaimById(anyInt())).thenReturn(null);
        assertThrows(BadRequestException.class, () -> {
            claimService.getObjectClaim(anyInt());
        });
    }

    @Test
    void registerObjectClaim() throws IOException {
        assertThrows(BadRequestException.class, () -> {
            claimService.registerObjectClaim(files, files, registerObjectClaimDAO);
        });
        when(files.getFirst().getOriginalFilename()).thenReturn("file1.txt");
        when(files.get(1).getOriginalFilename()).thenReturn("file2.txt");
        registerCarClaimDAO.setDocumentTypes(List.of("Document", "Document"));
        assertThrows(IOException.class, () -> {
            claimService.registerObjectClaim(files, files, registerObjectClaimDAO);
        });
    }

    @Test
    void getSharedClaims() {
    }

    @Test
    void shareClaim() {
        doNothing().when(claimRepo).shareClaim(any());
        assertEquals(0, claimService.shareClaim(any()));
    }

    @Test
    void updateClaimStatus() {
        doNothing().when(claimRepo).updateClaimStatus(any());
        claimService.updateClaimStatus(any());
    }
}