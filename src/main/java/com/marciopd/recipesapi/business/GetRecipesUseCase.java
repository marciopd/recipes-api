package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.GetRecipesRequest;
import com.marciopd.recipesapi.domain.GetRecipesResponse;

public interface GetRecipesUseCase {
    GetRecipesResponse getRecipes(GetRecipesRequest request);
}
