package com.dcs.cdr.charge.detail.record.repository;

import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChargeDetailRecordRepository extends MongoRepository<ChargeDetailRecord, String> {

    Optional<List<ChargeDetailRecord>> findByVehicleId(String vehicleId);

}
