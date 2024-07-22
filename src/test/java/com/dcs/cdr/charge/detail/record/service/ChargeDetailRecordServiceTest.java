package com.dcs.cdr.charge.detail.record.service;

import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import com.dcs.cdr.charge.detail.record.exception.CdrException;
import com.dcs.cdr.charge.detail.record.mapper.Mapper;
import com.dcs.cdr.charge.detail.record.repository.ChargeDetailRecordRepository;
import com.dcs.cdr.charge.detail.record.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class ChargeDetailRecordServiceTest {

    private final ChargeDetailRecordRepository repository = Mockito.mock(ChargeDetailRecordRepository.class);

    private final Mapper mapper = Mockito.mock(Mapper.class);

    private final RedisTemplate<String, Object> redisTemplate = Mockito.mock(RedisTemplate.class);

    private final ChargeDetailRecordService service = new ChargeDetailRecordService(repository, mapper, redisTemplate);

    @Test
    public void testCreateChargeDetailRecord() {
        CreateCdrRequest request = TestUtil.createValidCreateCdrRequest();
        var record = TestUtil.createValidChargeDetailRecord();

        when(mapper.map(any(CreateCdrRequest.class))).thenReturn(record);
        when(repository.save(any(ChargeDetailRecord.class))).thenReturn(record);
        String id = service.createChargeDetailRecord(request);

        assertEquals(record.getId(), id);
    }

    @Test
    public void testGetChargeDetailRecordById() {
        var record = TestUtil.createValidChargeDetailRecord();
        var response = TestUtil.createValidCdrResponse();

        when(repository.findById(anyString())).thenReturn(Optional.of(record));
        when(mapper.map(any(ChargeDetailRecord.class))).thenReturn(response);

        CdrResponse result = service.getChargeDetailRecordById(record.getId());

        assertEquals(response, result);
    }

    @Test
    public void testGetChargeDetailRecordByIdNotFound() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CdrException.class, () -> service.getChargeDetailRecordById("invalidId"));
    }

    @Test
    public void testSearchChargeDetailRecords() {
        SearchCdrRequest request = TestUtil.createValidSearchCdrRequest();
        var record = TestUtil.createValidChargeDetailRecord();
        var response = TestUtil.createValidCdrResponse();
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(Collections.singletonList(record), pageable, 1);

        when(mapper.map(any(SearchCdrRequest.class))).thenReturn(record);
        when(repository.findAll(any(), any(PageRequest.class))).thenReturn(page);
        when(mapper.map(any(ChargeDetailRecord.class))).thenReturn(response);

        var result = service.searchChargeDetailRecords(request);

        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().get(0));
    }
}
