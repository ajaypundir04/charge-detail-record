package com.dcs.cdr.charge.detail.record.service;

import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.exception.CdrException;
import com.dcs.cdr.charge.detail.record.mapper.Mapper;
import com.dcs.cdr.charge.detail.record.repository.ChargeDetailRecordRepository;
import com.dcs.cdr.charge.detail.record.util.CdrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChargeDetailRecordService {


    private final ChargeDetailRecordRepository repository;
    private final Mapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ChargeDetailRecordService(ChargeDetailRecordRepository repository,
                                     Mapper mapper, RedisTemplate<String, Object> redisTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
    }

    public String createChargeDetailRecord(CreateCdrRequest cdr) {
        var record = repository.save(this.mapper.map(cdr));
        evictCacheByVehicleId(record.getVehicleId());
        return record.getId();
    }

    @Cacheable(value = "chargeDetailRecords", key = "#id")
    public CdrResponse getChargeDetailRecordById(String id) {
        var chargeDetailRecord = repository.findById(id)
                .orElseThrow(() -> new CdrException(HttpStatusCode.valueOf(404), "Record not found"));
        return this.mapper.map(chargeDetailRecord);
    }


    @Cacheable(value = "chargeDetailRecords")
    public Page<CdrResponse> searchChargeDetailRecords(SearchCdrRequest request) {
        var probe = this.mapper.map(request);
        var example = CdrUtil.getChargeDetailRecordExample(probe);
        var sort = Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortField());
        var pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
        var recordPage = repository.findAll(example, pageable);
        log.info("Number of elements {}", recordPage.getTotalElements());
        var cdrResponses = recordPage.getContent().stream().map(this.mapper::map).toList();
        return new PageImpl<>(cdrResponses, pageable, recordPage.getTotalElements());
    }

    private void evictCacheByVehicleId(String vehicleId) {
        var pattern = "chargeDetailRecords::" + vehicleId + "*";
        var keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            log.info("Cache Evicted for vehicleId {}", vehicleId);
            redisTemplate.delete(keys);
        }
    }

}
