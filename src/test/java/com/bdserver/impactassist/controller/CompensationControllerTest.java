package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.RequestCompensationDAO;
import com.bdserver.impactassist.security.JwtFilter;
import com.bdserver.impactassist.service.CompensationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompensationController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtFilter.class
))
@DisabledInAotMode
class CompensationControllerTest {
    @MockBean
    private CompensationService compensationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void generatePdf() throws Exception {
        when(compensationService.generatePdf(any(RequestCompensationDAO.class))).thenReturn(new byte[]{0x01, 0x02});
        String json = objectMapper.writeValueAsString(new RequestCompensationDAO(1, new BigDecimal("10.02")));
        mockMvc.perform(post("/compensation").contentType(MediaType.APPLICATION_JSON).content(json).with(csrf())).andExpect(status().isOk());
    }
}