package com.dcs.cdr.charge.detail.record.dto;

import com.dcs.cdr.charge.detail.record.exception.CdrException;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;

@Getter
public enum SortableFields {
    START_TIME("startTime"), END_TIME("endTime");

    private final String fieldName;

    SortableFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public static SortableFields mapToFieldName(String fieldName) {
        return Arrays.stream(SortableFields.values()).filter(s -> fieldName.equalsIgnoreCase(s.fieldName)).findFirst().orElseThrow(() -> new CdrException(HttpStatusCode.valueOf(400), "Illegal field Name for sorting"));
    }

}
