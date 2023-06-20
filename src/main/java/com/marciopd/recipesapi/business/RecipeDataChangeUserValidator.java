package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.persistence.entity.RecipeEntity;

public interface RecipeDataChangeUserValidator {
    void validateIfCurrentUserCanChangeRecipe(RecipeEntity recipeEntity);
}
