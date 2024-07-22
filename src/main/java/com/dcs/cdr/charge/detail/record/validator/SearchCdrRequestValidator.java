package com.dcs.cdr.charge.detail.record.validator;

import com.dcs.cdr.charge.detail.record.dto.SearchCdrRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SearchCdrRequestValidator implements ConstraintValidator<ValidSearchCdrRequest, SearchCdrRequest> {
    @Override
    public boolean isValid(SearchCdrRequest searchCdrRequest, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> errorMessages = new ArrayList<>();

        if (Stream.of(searchCdrRequest.getVehicleId(), searchCdrRequest.getSessionId()).allMatch(StringUtils::isBlank)) {
            errorMessages.add("Please use combination of combination of [vehicleId, sessionId]");
        }
        errorMessages.forEach(errorMessage -> context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation());
        return errorMessages.isEmpty();
    }
}
