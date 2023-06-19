package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.GetRecipeResponse;

public interface GetRecipeUseCase {
    GetRecipeResponse getRecipe(Long id);
}
