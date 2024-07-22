package com.dcs.cdr.charge.detail.record.service;

import com.dcs.cdr.charge.detail.record.config.TestConfig;
import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import com.dcs.cdr.charge.detail.record.repository.ChargeDetailRecordRepository;
import com.dcs.cdr.charge.detail.record.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@EnableCaching
@Import(TestConfig.class)
public class ChargeDetailRecordServiceIntegrationTest {

    @Autowired
    private ChargeDetailRecordService service;

    @Autowired
    private ChargeDetailRecordRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        mongoTemplate.getDb().drop(); // Clean the database before each test
        cacheManager.getCache("chargeDetailRecords").clear(); // Clear the cache before each test
    }

    @Test
    public void testCreateChargeDetailRecord() {
        CreateCdrRequest request = TestUtil.createValidCreateCdrRequest();
        var record = TestUtil.createValidChargeDetailRecord();

        String id = service.createChargeDetailRecord(request);

        Optional<ChargeDetailRecord> savedRecord = repository.findById(id);
        assertEquals(true, savedRecord.isPresent());
        assertEquals(record.getVehicleId(), savedRecord.get().getVehicleId());
    }

    @Test
    public void testGetChargeDetailRecordById() {
        var record = TestUtil.createValidChargeDetailRecord();
        repository.save(record);

        // First call should fetch from DB and cache the result
        CdrResponse result = service.getChargeDetailRecordById(record.getId());
        assertEquals(record.getId(), result.getId());
        assertEquals(record.getVehicleId(), result.getVehicleId());

        // Second call should fetch from cache
        CdrResponse cachedResult = service.getChargeDetailRecordById(record.getId());
        assertEquals(result, cachedResult);
    }

    @Test
    public void testGetChargeDetailRecordByIdNotFound() {
        assertThrows(RuntimeException.class, () -> service.getChargeDetailRecordById("invalidId"));
    }

    @Test
    public void testSearchChargeDetailRecords() {
        SearchCdrRequest request = TestUtil.createValidSearchCdrRequest();
        var record = TestUtil.createValidChargeDetailRecord();
        repository.save(record);
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(Collections.singletonList(record), pageable, 1);

        var result = service.searchChargeDetailRecords(request);

        assertEquals(1, result.getTotalElements());
        assertEquals(record.getVehicleId(), result.getContent().get(0).getVehicleId());

        // Verify caching of search results
        var cachedResult = cacheManager.getCache("chargeDetailRecords").get(SimpleKeyGenerator.generateKey(new Object[]{request}), PageImpl.class);
        assertEquals(result, cachedResult);
    }

    @Test
    public void testEvictCacheOnCreate() {
        // Create and save a record
        CreateCdrRequest request = TestUtil.createValidCreateCdrRequest();
        var record = TestUtil.createValidChargeDetailRecord();
        var createdId = service.createChargeDetailRecord(request);

        // Ensure the record is cached
        service.getChargeDetailRecordById(createdId);
        assertEquals(createdId, cacheManager.getCache("chargeDetailRecords").get(createdId, CdrResponse.class).getId());

        // Create another record with the same vehicle ID to trigger cache eviction
        CreateCdrRequest newRequest = TestUtil.createValidCreateCdrRequest();
        newRequest.setVehicleId(record.getVehicleId()); // Same vehicle ID to trigger eviction
        service.createChargeDetailRecord(newRequest);

        // Verify the cache has been evicted
        assertNull(cacheManager.getCache("chargeDetailRecords").get(record.getId()));
    }
}
