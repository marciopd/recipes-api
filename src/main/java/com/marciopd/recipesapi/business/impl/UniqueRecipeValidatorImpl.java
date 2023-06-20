package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.UniqueRecipeValidator;
import com.marciopd.recipesapi.business.exception.DuplicatedRecipeException;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueRecipeValidatorImpl implements UniqueRecipeValidator {

    private final RecipeRepository recipeRepository;

    @Override
    public void validate(String title) throws DuplicatedRecipeException {
        if (recipeRepository.existsByTitle(title)) {
            throw new DuplicatedRecipeException();
        }
    }
}
