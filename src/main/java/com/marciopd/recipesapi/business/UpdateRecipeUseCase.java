package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.UpdateRecipeRequest;

public interface UpdateRecipeUseCase {
    void updateRecipe(Long recipeId, UpdateRecipeRequest request);
}
