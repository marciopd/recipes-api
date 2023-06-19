package com.marciopd.recipesapi.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicatedRecipeException extends ResponseStatusException {
    public DuplicatedRecipeException() {
        super(HttpStatus.BAD_REQUEST, "DUPLICATED_RECIPE");
    }
}
