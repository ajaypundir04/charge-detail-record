package com.dcs.cdr.charge.detail.record.util;

import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TestUtil {

    private static final LocalDateTime START_TIME = LocalDateTime.now();
    private static final LocalDateTime END_TIME = LocalDateTime.now().plusHours(1);

    public static CreateCdrRequest createValidCreateCdrRequest() {
        CreateCdrRequest request = new CreateCdrRequest();
        request.setSessionId("testSessionId");
        request.setVehicleId("testVehicleId");
        request.setStartTime(START_TIME);
        request.setEndTime(END_TIME);
        request.setTotalCost(15.0);
        return request;
    }

    public static SearchCdrRequest createValidSearchCdrRequest() {
        SearchCdrRequest request = new SearchCdrRequest();
        request.setVehicleId("testVehicleId");
        request.setSessionId("testSessionId");
        request.setSortField("startTime");
        request.setSortOrder("ASC");
        request.setPageNumber(0);
        request.setPageSize(10);
        return request;
    }

    public static CdrResponse createValidCdrResponse() {
        CdrResponse response = new CdrResponse();
        response.setId("testRecordId");
        response.setSessionId("testSessionId");
        response.setVehicleId("testVehicleId");
        response.setStartTime(START_TIME);
        response.setEndTime(END_TIME);
        response.setTotalCost(15.0);
        return response;
    }

    public static ChargeDetailRecord createValidChargeDetailRecord() {
        return ChargeDetailRecord.builder().id("testRecordId")
                .vehicleId("testVehicleId").sessionId("testSessionId")
                .totalCost(15.0).startTime(START_TIME).endTime(END_TIME).build();

    }

    public static List<CdrResponse> createValidCdrResponseList() {
        return Collections.singletonList(createValidCdrResponse());
    }
}
