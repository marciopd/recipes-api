package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.configuration.security.token.AccessToken;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeDataChangeUserValidatorImplTest {

    @Mock
    private AccessToken currentRequestUserTokenMock;

    @InjectMocks
    private RecipeDataChangeUserValidatorImpl recipeDataChangeUserValidator;

    @Test
    void validate_shouldReturn_whenUserIsAdmin() {
        when(currentRequestUserTokenMock.hasRole("ADMIN")).thenReturn(true);

        RecipeEntity recipeEntity = RecipeEntity.builder().build();
        recipeDataChangeUserValidator.validateIfCurrentUserCanChangeRecipe(recipeEntity);

        verify(currentRequestUserTokenMock).hasRole("ADMIN");
    }

    @Test
    void validate_shouldReturn_whenUserIsRecipeOwner() {
        when(currentRequestUserTokenMock.hasRole("ADMIN")).thenReturn(false);
        long userId = 1L;
        when(currentRequestUserTokenMock.getUserId()).thenReturn(userId);

        RecipeEntity recipeEntity = RecipeEntity.builder()
                .user(UserEntity.builder().id(userId).build())
                .build();
        recipeDataChangeUserValidator.validateIfCurrentUserCanChangeRecipe(recipeEntity);

        verify(currentRequestUserTokenMock).hasRole("ADMIN");
        verify(currentRequestUserTokenMock).getUserId();
    }
}