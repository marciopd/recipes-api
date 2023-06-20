package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.CreateRecipeRequest;
import com.marciopd.recipesapi.domain.CreateRecipeResponse;

public interface CreateRecipeUseCase {
    CreateRecipeResponse createRecipe(CreateRecipeRequest request);
}
