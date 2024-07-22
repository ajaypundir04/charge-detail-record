package com.dcs.cdr.charge.detail.record.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chargeDetailRecords")
@Data
@Builder
public class ChargeDetailRecord {
    @Id
    private String id;
    private String sessionId;
    @Indexed
    private String vehicleId;
    @Indexed
    private LocalDateTime startTime;
    @Indexed
    private LocalDateTime endTime;
    private Double totalCost;
}
