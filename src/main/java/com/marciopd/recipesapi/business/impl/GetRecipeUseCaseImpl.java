package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.GetRecipeUseCase;
import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.domain.GetRecipeResponse;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetRecipeUseCaseImpl implements GetRecipeUseCase {
    private final RecipeRepository recipeRepository;
    private final TagEntityConverter tagEntityConverter;

    @Transactional
    @Override
    public GetRecipeResponse getRecipe(Long id) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(id);
        RecipeEntity recipeEntity = optionalRecipe.orElseThrow(RecipeNotFoundException::new);

        return GetRecipeResponse.builder()
                .id(recipeEntity.getId())
                .title(recipeEntity.getTitle())
                .shortDescription(recipeEntity.getShortDescription())
                .instructions(recipeEntity.getInstructions())
                .userId(recipeEntity.getUserId())
                .numberServings(recipeEntity.getNumberServings())
                .tags(tagEntityConverter.toResponse(recipeEntity.getTags()))
                .ingredients(convertIngredients(recipeEntity))
                .creationTime(recipeEntity.getCreationTime())
                .build();
    }

    private static List<GetRecipeResponse.Ingredient> convertIngredients(RecipeEntity recipeEntity) {
        return recipeEntity.getIngredients().stream().map(ingredientEntity -> GetRecipeResponse.Ingredient.builder()
                .text(ingredientEntity.getText())
                .build()).toList();
    }
}
