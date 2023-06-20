package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class CreateRecipeResponse {
    private Long recipeId;
}
