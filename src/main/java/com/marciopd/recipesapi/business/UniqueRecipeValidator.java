package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.business.exception.DuplicatedRecipeException;

public interface UniqueRecipeValidator {
    void validate(String title) throws DuplicatedRecipeException;
}
