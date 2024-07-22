package com.dcs.cdr.charge.detail.record.validator;

import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import com.dcs.cdr.charge.detail.record.repository.ChargeDetailRecordRepository;
import com.dcs.cdr.charge.detail.record.util.CdrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class CreateCdrRequestValidator implements ConstraintValidator<ValidCreateCdrRequest, CreateCdrRequest> {

    private final ChargeDetailRecordRepository chargeDetailRecordRepository;

    public CreateCdrRequestValidator(ChargeDetailRecordRepository chargeDetailRecordRepository) {
        this.chargeDetailRecordRepository = chargeDetailRecordRepository;
    }

    @Override
    public boolean isValid(CreateCdrRequest createCdrRequest, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> errorMessages = new ArrayList<>();
        String previousEndTime = validatePreviousEndTime(createCdrRequest);
        if (Objects.nonNull(previousEndTime)) {
            errorMessages.add(previousEndTime);
        }
        errorMessages.addAll(Arrays.stream(CreateCdrRequestRules.Impl.values())
                .map(validator -> validator.validate(createCdrRequest))
                .filter(Objects::nonNull).toList());
        errorMessages.forEach(errorMessage -> context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation());
        return errorMessages.isEmpty();
    }

    private String validatePreviousEndTime(CreateCdrRequest createCdrRequest) {
        Optional<List<ChargeDetailRecord>> chargeDetailRecords = this.chargeDetailRecordRepository
                .findByVehicleId(createCdrRequest.getVehicleId());
        if (chargeDetailRecords.isPresent()) {
            LocalDateTime endTime =
                    chargeDetailRecords.get().stream()
                            .map(ChargeDetailRecord::getEndTime)
                            .filter(eTime-> eTime.isAfter(createCdrRequest.getStartTime()))
                            .findFirst()
                            .orElse(null);
            return Objects.nonNull(endTime) ?
                    String.format(CdrUtil.PREVIOUS_END_TIME_COST_ERR_MSG, createCdrRequest.getStartTime(), endTime)
                    : null;
        }
        return null;
    }

}
