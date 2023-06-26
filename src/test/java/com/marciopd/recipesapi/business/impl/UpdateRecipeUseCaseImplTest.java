package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.RecipeDataChangeUserValidator;
import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.UniqueRecipeValidator;
import com.marciopd.recipesapi.business.exception.DuplicatedRecipeException;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.business.exception.UnauthorizedDataAccessException;
import com.marciopd.recipesapi.domain.UpdateRecipeRequest;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.IngredientEntity;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRecipeUseCaseImplTest {

    @Mock
    private RecipeRepository recipeRepositoryMock;
    @Mock
    private TagEntityConverter tagEntityConverterMock;
    @Mock
    private UniqueRecipeValidator uniqueRecipeValidatorMock;
    @Mock
    private RecipeDataChangeUserValidator recipeDataChangeUserValidatorMock;

    @InjectMocks
    private UpdateRecipeUseCaseImpl updateRecipeUseCase;

    @Test
    void updateRecipe_shouldThrowNotFoundException_whenRecipeNotFound() {
        Long nonExistentId = 817212L;
        UpdateRecipeRequest request = UpdateRecipeRequest.builder().build();

        when(recipeRepositoryMock.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class,
                () -> updateRecipeUseCase.updateRecipe(nonExistentId, request));

        verify(recipeRepositoryMock).findById(nonExistentId);
    }

    @Test
    void updateRecipe_shouldThrowUnauthorizedDataException_whenInvalidUser() {
        Long recipeId = 817212L;
        UpdateRecipeRequest request = UpdateRecipeRequest.builder().build();

        RecipeEntity recipe = RecipeEntity.builder().id(recipeId).build();
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipe));

        doThrow(UnauthorizedDataAccessException.class)
                .when(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipe);

        assertThrows(UnauthorizedDataAccessException.class,
                () -> updateRecipeUseCase.updateRecipe(recipeId, request));

        verify(recipeRepositoryMock).findById(recipeId);
        verify(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipe);
    }

    @Test
    void updateRecipe_shouldDuplicatedRecipeException_whenTitleDuplicated() {
        Long recipeId = 12313L;
        String repeatedTitle = "Repeated title";
        UpdateRecipeRequest request = UpdateRecipeRequest.builder()
                .title(repeatedTitle)
                .build();

        RecipeEntity recipe = RecipeEntity.builder()
                .title("Old title")
                .id(recipeId)
                .build();
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipe));

        doThrow(DuplicatedRecipeException.class)
                .when(uniqueRecipeValidatorMock).validate(repeatedTitle);

        assertThrows(DuplicatedRecipeException.class,
                () -> updateRecipeUseCase.updateRecipe(recipeId, request));

        verify(recipeRepositoryMock).findById(recipeId);
        verify(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipe);
        verify(uniqueRecipeValidatorMock).validate(repeatedTitle);
    }

    @Test
    void updateRecipe_shouldUpdateAllFieldRecipe_whenRequestValid() {
        Long recipeId = 12313L;
        String newTitle = "New title";
        Set<Long> tagIds = Set.of(1L);
        UpdateRecipeRequest request = UpdateRecipeRequest.builder()
                .title(newTitle)
                .shortDescription("New short description")
                .tagIds(tagIds)
                .ingredients(List.of("ING 01"))
                .instructions("New instructions")
                .numberServings(5)
                .build();

        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .title("Old title")
                .shortDescription("Old short description")
                .instructions("Old instructions")
                .numberServings(2)
                .build();
        recipe.addTags(List.of(TagEntity.builder().id(55L).build()));
        recipe.addIngredients(List.of("OLD INGREDIENT"));

        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipe));

        List<TagEntity> newTagEntities = List.of(TagEntity.builder().id(1L).build());
        when(tagEntityConverterMock.toEntity(tagIds))
                .thenReturn(newTagEntities);

        RecipeEntity expectedRecipeToSave = RecipeEntity.builder()
                .id(recipeId)
                .title(newTitle)
                .shortDescription("New short description")
                .tags(newTagEntities)
                .ingredients(List.of(IngredientEntity.builder().text("ING 01").build()))
                .instructions("New instructions")
                .numberServings(5)
                .build();
        when(recipeRepositoryMock.save(expectedRecipeToSave)).thenReturn(null);

        updateRecipeUseCase.updateRecipe(recipeId, request);

        verify(recipeRepositoryMock).findById(recipeId);
        verify(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipe);
        verify(uniqueRecipeValidatorMock).validate(newTitle);
        verify(recipeRepositoryMock).flush();
        verify(tagEntityConverterMock).toEntity(tagIds);
        verify(recipeRepositoryMock).save(expectedRecipeToSave);
    }
}