package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetRecipesResponse {
    private List<Recipe> recipes;

    @Builder
    @Data
    public static class Recipe {
        private Long id;
        private String title;
    }
}
