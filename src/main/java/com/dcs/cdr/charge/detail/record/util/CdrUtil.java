package com.dcs.cdr.charge.detail.record.util;

import com.dcs.cdr.charge.detail.record.entity.ChargeDetailRecord;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;

import java.util.Objects;

public class CdrUtil {

    public static final String SMALLER_END_TIME_ERR_MSG = "The \"End time\" cannot be smaller than \"Start time\"";
    public static final String EQUAL_START_END_TIME_ERR_MSG = "The \"End time\" cannot be equal to \"Start time\"";
    public static final String ZERO_TOTAL_COST_ERR_MSG = "The \"Total cost\" must be greater than 0";
    public static final String PREVIOUS_END_TIME_COST_ERR_MSG = "\"Start time\" %s of an upcoming Charge Detail Record for a particular vehicle must always be bigger than the \"End time\" %s of any previous Charge Detail Records";
    private CdrUtil() {

    }

    public static Example<ChargeDetailRecord> getChargeDetailRecordExample(ChargeDetailRecord probe) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreNullValues();

        return Example.of(probe, matcher);
    }

}
