package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.RecipeDataChangeUserValidator;
import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.UniqueRecipeValidator;
import com.marciopd.recipesapi.business.UpdateRecipeUseCase;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.domain.UpdateRecipeRequest;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateRecipeUseCaseImpl implements UpdateRecipeUseCase {
    private final RecipeRepository recipeRepository;
    private final TagEntityConverter tagEntityConverter;
    private final UniqueRecipeValidator uniqueRecipeValidator;
    private final RecipeDataChangeUserValidator recipeDataChangeUserValidator;

    @Transactional
    @Override
    public void updateRecipe(Long recipeId, UpdateRecipeRequest request) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(recipeId);
        RecipeEntity recipeEntity = optionalRecipe.orElseThrow(RecipeNotFoundException::new);

        recipeDataChangeUserValidator.validateIfCurrentUserCanChangeRecipe(recipeEntity);

        if (!recipeEntity.getTitle().equals(request.getTitle())) {
            uniqueRecipeValidator.validate(request.getTitle());
        }

        deleteOldIngredientsAndTags(recipeEntity);
        updateRecipe(request, recipeEntity);
    }

    private void updateRecipe(UpdateRecipeRequest request, RecipeEntity recipeEntity) {
        List<TagEntity> tags = tagEntityConverter.toEntity(request.getTagIds());
        recipeEntity.setTitle(request.getTitle());
        recipeEntity.setShortDescription(request.getShortDescription());
        recipeEntity.setInstructions(request.getInstructions());
        recipeEntity.setNumberServings(request.getNumberServings());
        recipeEntity.addTags(tags);
        recipeEntity.addIngredients(request.getIngredients());

        recipeRepository.save(recipeEntity);
    }

    private void deleteOldIngredientsAndTags(RecipeEntity recipeEntity) {
        recipeEntity.clearTags();
        recipeEntity.clearIngredients();
        recipeRepository.flush();
    }
}
