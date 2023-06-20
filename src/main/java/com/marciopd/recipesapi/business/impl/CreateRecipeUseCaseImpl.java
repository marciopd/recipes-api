package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.CreateRecipeUseCase;
import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.UniqueRecipeValidator;
import com.marciopd.recipesapi.configuration.security.token.AccessToken;
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
    private final UniqueRecipeValidator uniqueRecipeValidator;
    private final TagEntityConverter tagEntityConverter;
    private final AccessToken currentRequestUser;

    @Transactional
    @Override
    public CreateRecipeResponse createRecipe(final CreateRecipeRequest request) {

        uniqueRecipeValidator.validate(request.getTitle());

        List<TagEntity> tags = tagEntityConverter.toEntity(request.getTagIds());

        RecipeEntity newRecipe = RecipeEntity.builder()
                .title(request.getTitle())
                .shortDescription(request.getShortDescription())
                .instructions(request.getInstructions())
                .numberServings(request.getNumberServings())
                .userId(currentRequestUser.getUserId())
                .tags(tags)
                .build();

        newRecipe.addIngredients(request.getIngredients());

        RecipeEntity savedRecipe = recipeRepository.save(newRecipe);

        return CreateRecipeResponse.builder()
                .recipeId(savedRecipe.getId())
                .build();
    }
}
