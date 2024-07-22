package com.dcs.cdr.charge.detail.record.validator;

import com.dcs.cdr.charge.detail.record.dto.CreateCdrRequest;
import com.dcs.cdr.charge.detail.record.util.CdrUtil;

public interface CreateCdrRequestRules {
    String validate(CreateCdrRequest createCdrRequest);

    enum Impl implements CreateCdrRequestRules {
        END_TIME {
            @Override
            public String validate(CreateCdrRequest createCdrRequest) {
                if (createCdrRequest.getEndTime().isBefore(createCdrRequest.getStartTime())) {
                    return CdrUtil.SMALLER_END_TIME_ERR_MSG;
                }
                return null;
            }
        },
        EQUAL_END_START_TIME {
            @Override
            public String validate(CreateCdrRequest createCdrRequest) {
                if (createCdrRequest.getEndTime().isEqual(createCdrRequest.getStartTime())) {
                    return CdrUtil.EQUAL_START_END_TIME_ERR_MSG;
                }
                return null;
            }
        }
    }
}
