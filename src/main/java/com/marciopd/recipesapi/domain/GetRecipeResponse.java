package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class GetRecipeResponse {
    private Long id;
    private User user;
    private String title;
    private String shortDescription;
    private String instructions;
    private Integer numberServings;
    private Instant creationTime;
    private List<Ingredient> ingredients;
    private List<TagResponse> tags;

    @Builder
    @Data
    public static class Ingredient {
        private String text;
    }

    @Builder
    @Data
    public static class User {
        private Long id;
        private String username;
    }
}
