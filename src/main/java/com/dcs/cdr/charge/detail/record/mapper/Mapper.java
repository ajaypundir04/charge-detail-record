package com.dcs.cdr.charge.detail.record.mapper;

import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import org.springframework.stereotype.Component;

public interface Mapper {

    ChargeDetailRecord map(CreateCdrRequest createCdrRequest);

    ChargeDetailRecord map(SearchCdrRequest searchCdrRequest);

    CdrResponse map(ChargeDetailRecord chargeDetailRecord);

    @Component
    class Impl implements Mapper {

        @Override
        public ChargeDetailRecord map(CreateCdrRequest createCdrRequest) {
          return ChargeDetailRecord.builder()
                          .vehicleId(createCdrRequest.getVehicleId())
                  .sessionId(createCdrRequest.getSessionId())
                  .totalCost(createCdrRequest.getTotalCost())
                  .startTime(createCdrRequest.getStartTime())
                  .endTime(createCdrRequest.getEndTime())
                  .build();
        }

        @Override
        public ChargeDetailRecord map(SearchCdrRequest searchCdrRequest) {
            return ChargeDetailRecord.builder()
                    .vehicleId(searchCdrRequest.getVehicleId())
                    .sessionId(searchCdrRequest.getSessionId())
                    .build();
        }

        @Override
        public CdrResponse map(ChargeDetailRecord chargeDetailRecord) {
            CdrResponse cdrResponse = new CdrResponse();
            cdrResponse.setId(chargeDetailRecord.getId());
            cdrResponse.setVehicleId(chargeDetailRecord.getVehicleId());
            cdrResponse.setTotalCost(chargeDetailRecord.getTotalCost());
            cdrResponse.setStartTime(chargeDetailRecord.getStartTime());
            cdrResponse.setEndTime(chargeDetailRecord.getEndTime());
            cdrResponse.setSessionId(chargeDetailRecord.getSessionId());
            return cdrResponse;
        }
    }
}
