package com.marciopd.recipesapi.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTagIdsException extends ResponseStatusException {
    public InvalidTagIdsException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_TAG_IDS");
    }
}
