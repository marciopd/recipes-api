package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.domain.GetRecipeResponse;
import com.marciopd.recipesapi.domain.GetRecipeResponse.Ingredient;
import com.marciopd.recipesapi.domain.TagResponse;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.IngredientEntity;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRecipeUseCaseImplTest {
    @Mock
    private RecipeRepository recipeRepositoryMock;
    @Mock
    private TagEntityConverter tagEntityConverterMock;
    @InjectMocks
    private GetRecipeUseCaseImpl getRecipeUseCase;

    @Test
    void getRecipe_shouldThrowRecipeNotFoundError_whenRecipeNotFound() {
        long recipeId = 101L;
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class,
                () -> getRecipeUseCase.getRecipe(recipeId));

        verify(recipeRepositoryMock).findById(recipeId);
    }

    @Test
    void getRecipe_shouldReturnAllFields_whenRecipeFound() {
        long recipeId = 101L;
        List<TagEntity> tagEntities = List.of(
                TagEntity.builder().id(1L).name("Tag 01").build(),
                TagEntity.builder().id(2L).name("Tag 02").build());
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .title("Title")
                .shortDescription("Short description")
                .instructions("Instructions")
                .user(UserEntity.builder().id(1L).username("username").build())
                .numberServings(2)
                .tags(tagEntities)
                .ingredients(List.of(
                        IngredientEntity.builder().text("Ingred 01").build(),
                        IngredientEntity.builder().text("Ingred 02").build()))
                .creationTime(Instant.parse("2007-12-03T10:15:30Z"))
                .build();
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipe));

        List<TagResponse> tagResponses = List.of(
                TagResponse.builder().id(1L).name("Tag 01").build(),
                TagResponse.builder().id(2L).name("Tag 02").build());
        when(tagEntityConverterMock.toResponse(tagEntities))
                .thenReturn(tagResponses);

        GetRecipeResponse response = getRecipeUseCase.getRecipe(recipeId);

        verify(recipeRepositoryMock).findById(recipeId);
        verify(tagEntityConverterMock).toResponse(tagEntities);

        GetRecipeResponse expected = GetRecipeResponse.builder()
                .id(recipeId)
                .title("Title")
                .shortDescription("Short description")
                .instructions("Instructions")
                .user(GetRecipeResponse.User.builder().id(1L).username("username").build())
                .numberServings(2)
                .tags(tagResponses)
                .ingredients(List.of(
                        Ingredient.builder().text("Ingred 01").build(),
                        Ingredient.builder().text("Ingred 02").build()))
                .creationTime(Instant.parse("2007-12-03T10:15:30Z"))
                .build();
        assertEquals(expected, response);
    }
}