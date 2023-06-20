package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.UniqueRecipeValidator;
import com.marciopd.recipesapi.business.exception.DuplicatedRecipeException;
import com.marciopd.recipesapi.configuration.security.token.AccessToken;
import com.marciopd.recipesapi.domain.CreateRecipeRequest;
import com.marciopd.recipesapi.domain.CreateRecipeResponse;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRecipeUseCaseImplTest {

    @Mock
    private RecipeRepository recipeRepositoryMock;
    @Mock
    private TagEntityConverter tagEntityConverterMock;
    @Mock
    private UniqueRecipeValidator uniqueRecipeValidatorMock;
    @Mock
    private AccessToken currentRequestUserTokenMock;

    @InjectMocks
    private CreateRecipeUseCaseImpl createRecipeUseCase;

    @Test
    void createRecipe_shouldThrowDuplicatedException_whenTitleExists() {
        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .title("Pizza")
                .build();

        doThrow(DuplicatedRecipeException.class)
                .when(uniqueRecipeValidatorMock)
                .validate("Pizza");

        assertThrows(DuplicatedRecipeException.class, () -> createRecipeUseCase.createRecipe(request));

        verify(uniqueRecipeValidatorMock).validate("Pizza");
    }

    @Test
    void createRecipe_shouldInsertRecipe_whenAllFieldsArePresent() {
        Set<Long> tagIds = Set.of(1L, 2L);
        CreateRecipeRequest request = createTestRequest(tagIds);

        List<TagEntity> tagEntities = List.of(
                TagEntity.builder().id(1L).name("Vegetarian").build(),
                TagEntity.builder().id(2L).name("Italian").build());
        when(tagEntityConverterMock.toEntity(tagIds)).thenReturn(tagEntities);

        when(currentRequestUserTokenMock.getUserId()).thenReturn(1L);

        RecipeEntity expectedEntity = RecipeEntity.builder()
                .title("Pizza Margherita")
                .shortDescription("The best pizza margherita you will ever have.")
                .instructions("After preparing dough, keep Pizza in oven for 10 min.")
                .userId(1L)
                .numberServings(2)
                .build();

        expectedEntity.setIngredients(createTestIngredients(expectedEntity));
        expectedEntity.setTags(tagEntities);

        RecipeEntity savedEntity = RecipeEntity.builder().id(100L).build();
        when(recipeRepositoryMock.save(expectedEntity)).thenReturn(savedEntity);

        CreateRecipeResponse response = createRecipeUseCase.createRecipe(request);

        CreateRecipeResponse expectedResponse = CreateRecipeResponse.builder().recipeId(100L).build();
        assertEquals(expectedResponse, response);

        verify(uniqueRecipeValidatorMock).validate(request.getTitle());
        verify(tagEntityConverterMock).toEntity(tagIds);
        verify(currentRequestUserTokenMock).getUserId();
        verify(recipeRepositoryMock).save(expectedEntity);
    }

    private static List<IngredientEntity> createTestIngredients(RecipeEntity expectedEntity) {
        return List.of(
                IngredientEntity.builder()
                        .recipe(expectedEntity)
                        .text("112-inch round of pizza dough, stretched").build(),
                IngredientEntity.builder()
                        .recipe(expectedEntity)
                        .text("3 tablespoons tomato sauce ").build(),
                IngredientEntity.builder()
                        .recipe(expectedEntity)
                        .text("Extra-virgin olive oil").build(),
                IngredientEntity.builder()
                        .recipe(expectedEntity)
                        .text("2¾ ounces fresh mozzarella").build(),
                IngredientEntity.builder()
                        .recipe(expectedEntity)
                        .text("4 to 5basil leaves, roughly torn").build());
    }

    private static CreateRecipeRequest createTestRequest(Set<Long> tagIds) {
        return CreateRecipeRequest.builder()
                .title("Pizza Margherita")
                .shortDescription("The best pizza margherita you will ever have.")
                .instructions("After preparing dough, keep Pizza in oven for 10 min.")
                .numberServings(2)
                .ingredients(List.of(
                        "112-inch round of pizza dough, stretched",
                        "3 tablespoons tomato sauce ",
                        "Extra-virgin olive oil",
                        "2¾ ounces fresh mozzarella",
                        "4 to 5basil leaves, roughly torn"
                ))
                .tagIds(tagIds)
                .build();
    }
}