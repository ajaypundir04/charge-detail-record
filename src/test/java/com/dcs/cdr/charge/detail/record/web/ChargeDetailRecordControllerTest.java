package com.dcs.cdr.charge.detail.record.web;

import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import com.dcs.cdr.charge.detail.record.repository.ChargeDetailRecordRepository;
import com.dcs.cdr.charge.detail.record.service.ChargeDetailRecordService;
import com.dcs.cdr.charge.detail.record.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChargeDetailRecordController.class)
public class ChargeDetailRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChargeDetailRecordService service;

    @MockBean
    private ChargeDetailRecordRepository chargeDetailRecordRepository;

    @Test
    public void testCreateChargeDetailRecord() throws Exception {
        CreateCdrRequest request = TestUtil.createValidCreateCdrRequest();

        when(service.createChargeDetailRecord(any(CreateCdrRequest.class))).thenReturn("recordId");

        mockMvc.perform(post("/api/cdr").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isCreated()).andExpect(jsonPath("$", is("recordId")));
    }

    @Test
    public void testCreateChargeDetailRecordBadRequest() throws Exception {
        CreateCdrRequest request = TestUtil.createValidCreateCdrRequest();

        when(chargeDetailRecordRepository.findByVehicleId(anyString())).thenReturn(Optional.of(List.of(ChargeDetailRecord.builder().endTime(LocalDateTime.now().plusHours(1)).build())));

        mockMvc.perform(post("/api/cdr").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateChargeDetailRecordInputBadRequest() throws Exception {
        CreateCdrRequest request = TestUtil.createValidCreateCdrRequest();
        request.setEndTime(LocalDateTime.now().minusHours(2));

        mockMvc.perform(post("/api/cdr").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isBadRequest());
        request = TestUtil.createValidCreateCdrRequest();
        request.setTotalCost(0.0);
        mockMvc.perform(post("/api/cdr").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isBadRequest());

        request = TestUtil.createValidCreateCdrRequest();
        request.setEndTime(request.getStartTime());
        mockMvc.perform(post("/api/cdr").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetChargeDetailRecordById() throws Exception {
        CdrResponse response = TestUtil.createValidCdrResponse();

        when(service.getChargeDetailRecordById(anyString())).thenReturn(response);

        mockMvc.perform(get("/api/cdr/{id}", "testRecordId")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is("testRecordId"))).andExpect(jsonPath("$.sessionId", is("testSessionId"))).andExpect(jsonPath("$.vehicleId", is("testVehicleId"))).andExpect(jsonPath("$.totalCost", is(15.0)));
    }

    @Test
    public void testGetChargeDetailRecordByIdBadRequest() throws Exception {

        mockMvc.perform(get("/api/cdr/{id}", " ")).andExpect(status().isBadRequest());

    }

    @Test
    public void testSearchChargeDetailRecords() throws Exception {
        Page<CdrResponse> cdrResponsePage = new PageImpl<>(TestUtil.createValidCdrResponseList());


        when(service.searchChargeDetailRecords(any(SearchCdrRequest.class))).thenReturn(cdrResponsePage);

        mockMvc.perform(get("/api/cdr/search").param("vehicleId", "testVehicleId")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id", is("testRecordId"))).andExpect(jsonPath("$.content[0].sessionId", is("testSessionId"))).andExpect(jsonPath("$.content[0].vehicleId", is("testVehicleId"))).andExpect(jsonPath("$.content[0].totalCost", is(15.0)));
    }

    @Test
    public void testSearchChargeDetailRecordsBadRequest() throws Exception {
        Page<CdrResponse> cdrResponsePage = new PageImpl<>(TestUtil.createValidCdrResponseList());


        when(service.searchChargeDetailRecords(any(SearchCdrRequest.class))).thenReturn(cdrResponsePage);

        mockMvc.perform(get("/api/cdr/search").param("vehicleId", "")).andExpect(status().isBadRequest());
    }
}

