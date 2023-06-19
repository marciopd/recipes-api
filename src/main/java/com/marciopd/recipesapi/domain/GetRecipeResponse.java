package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class GetRecipeResponse {
    private Long id;
    private Long userId;
    private String title;
    private String shortDescription;
    private String instructions;
    private Integer numberServings;
    private Instant creationTime;
    private List<Ingredient> ingredients;
    private List<Tag> tags;

    @Builder
    @Data
    public static class Tag {
        private Long id;
        private String name;
    }

    @Builder
    @Data
    public static class Ingredient {
        private String text;
    }
}
