package com.marciopd.recipesapi.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipeNotFoundException extends ResponseStatusException {
    public RecipeNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
