package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateRecipeResponse {
    private Long recipeId;
}
