package com.dcs.cdr.charge.detail.record.web;

import com.dcs.cdr.charge.detail.record.dto.CdrResponse;
import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.dto.PagedResponse;
import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import com.dcs.cdr.charge.detail.record.service.ChargeDetailRecordService;
import com.dcs.cdr.charge.detail.record.validator.ValidSearchCdrRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/cdr")
@Validated
public class ChargeDetailRecordController {

    private final ChargeDetailRecordService service;

    @Autowired
    public ChargeDetailRecordController(ChargeDetailRecordService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createChargeDetailRecord(@Valid @RequestBody CreateCdrRequest cdr) {
        return service.createChargeDetailRecord(cdr);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CdrResponse getChargeDetailRecordById(@Valid @PathVariable @NotBlank(message = "cdr id is required") String id) {
        return service.getChargeDetailRecordById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<CdrResponse> searchChargeDetailRecords(@ValidSearchCdrRequest SearchCdrRequest request) {
        return new PagedResponse<>(service.searchChargeDetailRecords(request));
    }
}
