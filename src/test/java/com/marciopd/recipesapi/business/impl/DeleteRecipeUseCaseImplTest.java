package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.RecipeDataChangeUserValidator;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.business.exception.UnauthorizedDataAccessException;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeUseCaseImplTest {
    @Mock
    private RecipeRepository recipeRepositoryMock;
    @Mock
    private RecipeDataChangeUserValidator recipeDataChangeUserValidatorMock;
    @InjectMocks
    private DeleteRecipeUseCaseImpl deleteRecipeUseCase;

    @Test
    void deleteRecipe_shouldThrowNotFoundException_WhenNotFound() {
        long recipeId = 19L;
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class,
                () -> deleteRecipeUseCase.deleteRecipe(recipeId));

        verify(recipeRepositoryMock).findById(recipeId);
        verifyNoMoreInteractions(recipeRepositoryMock);
        verifyNoInteractions(recipeDataChangeUserValidatorMock);
    }


    @Test
    void deleteRecipe_shouldThrowError_WhenUserNotAllowedChangeRecipe() {
        long recipeId = 20L;
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .id(recipeId)
                .title("Picanha BBQ II")
                .build();
        recipeEntity.addIngredients(List.of("Salt", "Picanha"));
        recipeEntity.addTags(List.of(TagEntity.builder().id(1L).build()));

        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipeEntity));

        doThrow(UnauthorizedDataAccessException.class)
                .when(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipeEntity);

        assertThrows(UnauthorizedDataAccessException.class, () -> deleteRecipeUseCase.deleteRecipe(recipeId));

        assertThat(recipeEntity.getIngredients()).hasSize(2);
        assertThat(recipeEntity.getTags()).hasSize(1);

        verify(recipeRepositoryMock).findById(recipeId);
        verify(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipeEntity);
        verifyNoMoreInteractions(recipeRepositoryMock);
    }

    @Test
    void deleteRecipe_shouldClearCascadeRelationshipsAndDelete_WhenRecipeExists() {
        long recipeId = 20L;
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .id(recipeId)
                .title("Picanha BBQ")
                .build();
        recipeEntity.addIngredients(List.of("Salt", "Picanha"));
        recipeEntity.addTags(List.of(TagEntity.builder().id(1L).build()));

        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipeEntity));

        deleteRecipeUseCase.deleteRecipe(recipeId);

        assertThat(recipeEntity.getIngredients()).isEmpty();
        assertThat(recipeEntity.getTags()).isEmpty();

        verify(recipeRepositoryMock).findById(recipeId);
        verify(recipeRepositoryMock).delete(recipeEntity);
        verify(recipeDataChangeUserValidatorMock).validateIfCurrentUserCanChangeRecipe(recipeEntity);
    }
}