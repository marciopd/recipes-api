package com.marciopd.recipesapi.domain;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
public class CreateRecipeRequest {
    @NotNull
    @Min(0)
    private Long userId;

    @NotBlank
    @Length(min = 3, max = 50)
    private String title;

    @NotBlank
    @Length(min = 20, max = 300)
    private String shortDescription;

    @NotBlank
    @Length(min = 20, max = 5000)
    private String instructions;

    @NotNull
    @Min(1)
    @Max(50)
    private Integer numberServings;

    @Size(min = 1, max = 100)
    private List<String> ingredients;

    @Size(max = 10)
    private Set<Long> tagIds;

    public List<String> getIngredients() {
        if (ingredients == null) {
            return Collections.emptyList();
        }

        return ingredients;
    }

    public Set<Long> getTagIds() {
        if (tagIds == null) {
            return Collections.emptySet();
        }
        return tagIds;
    }
}
