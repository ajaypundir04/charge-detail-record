package com.dcs.cdr.charge.detail.record.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class CdrException extends ResponseStatusException {

    public CdrException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

}
