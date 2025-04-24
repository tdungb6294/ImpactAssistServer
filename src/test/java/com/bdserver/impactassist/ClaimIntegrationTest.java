package com.bdserver.impactassist;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.service.S3Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@Import({TestcontainersConfiguration.class, TestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClaimIntegrationTest {
    private final LocalDate now = LocalDate.now();
    private String accessToken = "";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private S3Service s3Service;

    @BeforeAll
    public void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@email.com\", \"password\":\"admin\"}")
        ).andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        JwtTokenDAO token = objectMapper.readValue(json, JwtTokenDAO.class);
        accessToken = token.getAccessToken();
    }

    @Test
    @Order(1)
    public void testCarClaimRegistration() throws Exception {
        doNothing().when(s3Service).uploadFile(any(), any());
        List<String> documentTypes = List.of("document");
        RegisterCarClaimDAO claim = RegisterCarClaimDAO.builder()
                .carModel("BMW M5")
                .vehicleRegistrationNumber("LD72 WQR")
                .vehicleIdentificationNumber("JTDKBRFU4E3546711")
                .odometerMileage("57482km")
                .insurancePolicyNumber("IP1234567890")
                .insuranceCompany("ERGO Vienna")
                .accidentDatetime(now.atTime(0, 0))
                .locationLatitude(0.102669)
                .locationLongitude(51.363850)
                .address("123 Baker Street, London, NW1 6XE, United Kingdom")
                .description("BMW M5 crashe into a door.")
                .policeInvolved(false)
                .weatherCondition("Sunny")
                .compensationMethod("Bank account")
                .dataManagementConsent(true)
                .internationalBankAccountNumber("GB29NWBK60161331926819")
                .documentTypes(documentTypes)
                .build();
        String json = objectMapper.writeValueAsString(claim);

        MockMultipartFile document = new MockMultipartFile("documents", "document.jpg", "image/jpeg", "document".getBytes());
        MockMultipartFile image = new MockMultipartFile("images", "image.jpg", "image/jpeg", "image".getBytes());
        MockMultipartFile jsonPart = new MockMultipartFile("data", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        mockMvc.perform(multipart("/claim/car")
                        .file(document)
                        .file(image)
                        .file(jsonPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @Order(2)
    public void testObjectClaimRegistration() throws Exception {
        doNothing().when(s3Service).uploadFile(any(), any());
        List<String> documentTypes = List.of("document");
        RegisterObjectClaimDAO object = RegisterObjectClaimDAO.builder()
                .objectType("Door")
                .objectMaterial("Wood")
                .objectOwnership("Private")
                .damageToObjectDescription("The lock was broken")
                .insurancePolicyNumber("IP1234567890")
                .insuranceCompany("ERGO Vienna")
                .accidentDatetime(now.atTime(0, 0))
                .locationLatitude(0.102669)
                .locationLongitude(51.363850)
                .address("123 Baker Street, London, NW1 6XE, United Kingdom")
                .description("BMW M5 crashe into a door.")
                .policeInvolved(false)
                .weatherCondition("Sunny")
                .compensationMethod("Bank account")
                .dataManagementConsent(true)
                .internationalBankAccountNumber("GB29NWBK60161331926819")
                .documentTypes(documentTypes)
                .build();
        String json = objectMapper.writeValueAsString(object);

        MockMultipartFile document = new MockMultipartFile("documents", "document.jpg", "image/jpeg", "document".getBytes());
        MockMultipartFile image = new MockMultipartFile("images", "image.jpg", "image/jpeg", "image".getBytes());
        MockMultipartFile jsonPart = new MockMultipartFile("data", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        mockMvc.perform(multipart("/claim/object")
                        .file(document)
                        .file(image)
                        .file(jsonPart)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    @Order(3)
    public void getCarClaim() throws Exception {
        when(s3Service.getSignedUrl(any())).thenReturn(new URL("http://localhost:8080/file"));
        MvcResult result = mockMvc.perform(get("/claim/car/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        CarClaimDAO claimDAO = objectMapper.readValue(json, CarClaimDAO.class);

        List<String> expectedImages = List.of("http://localhost:8080/file");
        List<PartialClaimDocumentDAO> documents = List.of(new PartialClaimDocumentDAO("http://localhost:8080/file", "document"));
        assertEquals(ClaimStatus.PENDING, claimDAO.getClaimStatus());
        assertEquals(1, claimDAO.getId());
        assertEquals("BMW M5", claimDAO.getCarModel());
        assertEquals("LD72 WQR", claimDAO.getVehicleRegistrationNumber());
        assertEquals("JTDKBRFU4E3546711", claimDAO.getVehicleIdentificationNumber());
        assertEquals("57482km", claimDAO.getOdometerMileage());
        assertEquals("IP1234567890", claimDAO.getInsurancePolicyNumber());
        assertEquals("ERGO Vienna", claimDAO.getInsuranceCompany());
        assertEquals(now.atTime(0, 0), claimDAO.getAccidentDatetime());
        assertEquals(0.102669, claimDAO.getLocationLatitude());
        assertEquals(51.363850, claimDAO.getLocationLongitude());
        assertEquals("123 Baker Street, London, NW1 6XE, United Kingdom", claimDAO.getAddress());
        assertEquals("BMW M5 crashe into a door.", claimDAO.getDescription());
        assertEquals("Sunny", claimDAO.getWeatherCondition());
        assertEquals("Bank account", claimDAO.getCompensationMethod());
        assertEquals("GB29NWBK60161331926819", claimDAO.getInternationalBankAccountNumber());
        assertEquals(expectedImages, claimDAO.getClaimAccidentImageUrls());
        assertEquals(documents, claimDAO.getClaimAccidentDocuments());
    }

    @Test
    @Order(4)
    public void getClaims() throws Exception {
        MvcResult result = mockMvc.perform(get("/claim")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        Map<String, Object> mapResult = objectMapper.readValue(json, new TypeReference<>() {
        });
        Integer currentPage = (Integer) mapResult.get("currentPage");
        Integer nextPage = (Integer) mapResult.get("nextPage");
        Integer totalPages = (Integer) mapResult.get("totalPages");
        Integer total = (Integer) mapResult.get("total");
        assertEquals(1, currentPage);
        assertEquals(1, totalPages);
        assertNull(nextPage);
        assertEquals(2, total);
    }

    @Test
    @Order(5)
    public void getObjectClaim() throws Exception {
        when(s3Service.getSignedUrl(any())).thenReturn(new URL("http://localhost:8080/file"));
        MvcResult result = mockMvc.perform(get("/claim/object/2")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectClaimDAO claimDAO = objectMapper.readValue(json, ObjectClaimDAO.class);

        List<String> expectedImages = List.of("http://localhost:8080/file");
        List<PartialClaimDocumentDAO> documents = List.of(new PartialClaimDocumentDAO("http://localhost:8080/file", "document"));
        assertEquals(ClaimStatus.PENDING, claimDAO.getClaimStatus());
        assertEquals(2, claimDAO.getId());
        assertEquals("Door", claimDAO.getObjectType());
        assertEquals("Wood", claimDAO.getObjectMaterial());
        assertEquals(ObjectOwnership.Private, claimDAO.getObjectOwnership());
        assertEquals("The lock was broken", claimDAO.getDamageToObjectDescription());
        assertEquals("IP1234567890", claimDAO.getInsurancePolicyNumber());
        assertEquals("ERGO Vienna", claimDAO.getInsuranceCompany());
        assertEquals(now.atTime(0, 0), claimDAO.getAccidentDatetime());
        assertEquals(0.102669, claimDAO.getLocationLatitude());
        assertEquals(51.363850, claimDAO.getLocationLongitude());
        assertEquals("123 Baker Street, London, NW1 6XE, United Kingdom", claimDAO.getAddress());
        assertEquals("BMW M5 crashe into a door.", claimDAO.getDescription());
        assertEquals("Sunny", claimDAO.getWeatherCondition());
        assertEquals("Bank account", claimDAO.getCompensationMethod());
        assertEquals("GB29NWBK60161331926819", claimDAO.getInternationalBankAccountNumber());
        assertEquals(expectedImages, claimDAO.getClaimAccidentImageUrls());
        assertEquals(documents, claimDAO.getClaimAccidentDocuments());
    }

    @Test
    @Order(6)
    public void getCarClaims() throws Exception {
        MvcResult result = mockMvc.perform(get("/claim/car")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        Map<String, Object> mapResult = objectMapper.readValue(json, new TypeReference<>() {
        });
        Integer currentPage = (Integer) mapResult.get("currentPage");
        Integer nextPage = (Integer) mapResult.get("nextPage");
        Integer totalPages = (Integer) mapResult.get("totalPages");
        Integer total = (Integer) mapResult.get("total");
        List<LinkedHashMap> raw = (List<LinkedHashMap>) mapResult.get("claims");
        List<PartialClaimDAO> claims = raw.stream().map(item -> objectMapper.convertValue(item, PartialClaimDAO.class)).toList();
        assertEquals(1, currentPage);
        assertEquals(1, totalPages);
        assertNull(nextPage);
        assertEquals(1, total);
        PartialClaimDAO claim = claims.getFirst();
        assertEquals(ClaimStatus.PENDING, claim.getClaimStatus());
        assertEquals("BMW M5", claim.getCarModel());
        assertEquals(1, claim.getId());
        assertEquals("123 Baker Street, London, NW1 6XE, United Kingdom", claim.getAddress());
        assertEquals(now.atTime(0, 0), claim.getAccidentDatetime());
        assertNull(claim.getObjectType());
    }

    @Test
    @Order(7)
    public void createDamageReport() throws Exception {
        RequestDamageReportDAO requestDamageReportDAO = RequestDamageReportDAO.builder()
                .claimId(1)
                .autoPartsAndServices(List.of(1, 2, 3))
                .build();

        String jsonContent = objectMapper.writeValueAsString(requestDamageReportDAO);

        mockMvc.perform(post("/damage-report").header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk()).andExpect(content().string("1"));

        MvcResult result = mockMvc.perform(get("/damage-report/claim/1")
                .header("Authorization", "Bearer " + accessToken)).andReturn();
        String json = result.getResponse().getContentAsString();

        List<PartialDamageReportDAO> reports = objectMapper.readValue(json, new TypeReference<>() {
        });
        PartialDamageReportDAO damageReport = reports.getFirst();
        assertEquals(1, reports.size());
        assertEquals(1, damageReport.getReportId());
        assertEquals("admin", damageReport.getFullName());
    }

    @Test
    @Order(8)
    public void getDamageReport() throws Exception {
        MvcResult result = mockMvc.perform(get("/damage-report/1")
                .header("Authorization", "Bearer " + accessToken)).andReturn();
        String json = result.getResponse().getContentAsString();
        ResponseDamageReportDAO responseDamageReport = objectMapper.readValue(json, ResponseDamageReportDAO.class);
        DamageReportDAO damageReport = responseDamageReport.getDamageReport();
        assertEquals(1, damageReport.getReportId());
        assertEquals("admin", damageReport.getFullName());
        assertEquals(new BigDecimal("270.00"), damageReport.getEstimatedMinPriceWithoutService());
        assertEquals(new BigDecimal("1580.00"), damageReport.getEstimatedMaxPriceWithoutService());
        assertEquals(new BigDecimal("570.00"), damageReport.getEstimatedMinPriceWithService());
        assertEquals(new BigDecimal("3080.00"), damageReport.getEstimatedMaxPriceWithService());
        assertEquals(3, responseDamageReport.getAutoPartsAndServices().size());
    }
}
