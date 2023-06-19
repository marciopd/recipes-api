package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.DeleteRecipeUseCase;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRecipeUseCaseImpl implements DeleteRecipeUseCase {
    private final RecipeRepository recipeRepository;

    @Transactional
    @Override
    public void deleteRecipe(Long id) {
        RecipeEntity recipeEntity = recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);

        recipeEntity.clearIngredients();
        recipeEntity.clearTags();

        recipeRepository.delete(recipeEntity);
    }
}
