package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.CreateRecipeUseCase;
import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.exception.DuplicatedRecipeException;
import com.marciopd.recipesapi.domain.CreateRecipeRequest;
import com.marciopd.recipesapi.domain.CreateRecipeResponse;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRecipeUseCaseImpl implements CreateRecipeUseCase {

    private final RecipeRepository recipeRepository;

    private final TagEntityConverter tagEntityConverter;

    @Transactional
    @Override
    public CreateRecipeResponse createRecipe(final CreateRecipeRequest request) {
        if (recipeRepository.existsByTitle(request.getTitle())) {
            throw new DuplicatedRecipeException();
        }

        List<TagEntity> tags = tagEntityConverter.convertToEntity(request.getTagIds());

        RecipeEntity newRecipe = RecipeEntity.builder()
                .title(request.getTitle())
                .shortDescription(request.getShortDescription())
                .instructions(request.getInstructions())
                .numberServings(request.getNumberServings())
                .userId(request.getUserId())
                .tags(tags)
                .build();

        newRecipe.addIngredients(request.getIngredients());

        RecipeEntity savedRecipe = recipeRepository.save(newRecipe);

        return CreateRecipeResponse.builder()
                .recipeId(savedRecipe.getId())
                .build();
    }
}
