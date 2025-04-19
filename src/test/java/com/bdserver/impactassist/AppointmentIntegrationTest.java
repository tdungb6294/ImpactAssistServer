package com.bdserver.impactassist;

import com.bdserver.impactassist.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@Import({TestcontainersConfiguration.class, TestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentIntegrationTest {
    private final LocalDate earliestMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    private AvailabilityDAO firstSlot;
    private String accessToken;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

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
    public void getExpertAvailability() throws Exception {
        MvcResult result = mockMvc.perform(get("/local_expert_availability/1").header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        AvailabilitySummaryDAO summary = objectMapper.readValue(json, AvailabilitySummaryDAO.class);
        List<AvailabilityDAO> availableSlots = summary.getSlots();
        List<UnavailabilityDAO> unavailableSlots = summary.getUnavailableSlots();
        firstSlot = availableSlots.getFirst();
        assertEquals(4, availableSlots.size());
        assertEquals(0, unavailableSlots.size());
        assertEquals("Monday", firstSlot.getDayOfWeek());
    }

    @Test
    @Order(2)
    public void createAppointment() throws Exception {
        RequestAppointmentDAO appointment = RequestAppointmentDAO.builder()
                .title("Appointment Title")
                .description("Appointment Description")
                .userId(2)
                .appointmentDate(earliestMonday)
                .expertAvailabilityId(firstSlot.getId())
                .build();
        mockMvc.perform(post("/appointment")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        MvcResult result = mockMvc.perform(get("/local_expert_availability/1").header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        AvailabilitySummaryDAO summary = objectMapper.readValue(json, AvailabilitySummaryDAO.class);
        List<UnavailabilityDAO> unavailableSlots = summary.getUnavailableSlots();
        assertEquals(1, unavailableSlots.size());
        UnavailabilityDAO unavailableSlot = unavailableSlots.getFirst();
        assertEquals(earliestMonday, unavailableSlot.getDate());
        assertEquals(1, unavailableSlot.getAvailabilityId());
    }

    @Test
    @Order(3)
    public void getAllAppointments() throws Exception {
        MvcResult result = mockMvc.perform(get("/appointment")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        Map<String, Object> mapResult = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        Integer currentPage = (Integer) mapResult.get("currentPage");
        Integer nextPage = (Integer) mapResult.get("nextPage");
        Integer totalPages = (Integer) mapResult.get("totalPages");
        Integer total = (Integer) mapResult.get("total");
        List<LinkedHashMap> raw = (List<LinkedHashMap>) mapResult.get("appointments");
        List<AppointmentDAO> appointments = raw.stream().map(item -> objectMapper.convertValue(item, AppointmentDAO.class)).toList();
        assertEquals(1, currentPage);
        assertEquals(1, totalPages);
        assertNull(nextPage);
        assertEquals(1, total);
        AppointmentDAO appointment = appointments.getFirst();
        assertEquals(earliestMonday, appointment.getDate());
        assertEquals(1, appointment.getAvailabilityId());
        assertEquals(2, appointment.getUserId());
        assertEquals("Appointment Title", appointment.getTitle());
        assertEquals("Appointment Description", appointment.getDescription());
    }
}
