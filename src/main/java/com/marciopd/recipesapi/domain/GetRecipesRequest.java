package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetRecipesRequest {
    private List<String> withIngredients;
    private List<String> withoutIngredients;
    private List<Long> withTags;
    private List<Long> withoutTags;
    private String instruction;
    private Integer numberServings;
}
