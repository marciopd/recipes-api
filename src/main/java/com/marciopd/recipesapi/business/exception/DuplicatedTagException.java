package com.marciopd.recipesapi.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicatedTagException extends ResponseStatusException {
    public DuplicatedTagException() {
        super(HttpStatus.BAD_REQUEST, "DUPLICATED_TAG");
    }
}
