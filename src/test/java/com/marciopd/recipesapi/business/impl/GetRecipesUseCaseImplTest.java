package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.domain.GetRecipesRequest;
import com.marciopd.recipesapi.domain.GetRecipesResponse;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.marciopd.recipesapi.business.impl.GetRecipesUseCaseImpl.SORT_BY_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRecipesUseCaseImplTest {

    @Mock
    private RecipeRepository recipeRepositoryMock;

    @InjectMocks
    private GetRecipesUseCaseImpl getRecipesUseCase;

    @Test
    void getRecipes_shouldFindAll_whenNoSpecs() {
        GetRecipesRequest request = GetRecipesRequest.builder().build();

        when(recipeRepositoryMock.findAll(SORT_BY_TITLE)).thenReturn(createTestRecipes());

        GetRecipesResponse response = getRecipesUseCase.getRecipes(request);

        verify(recipeRepositoryMock).findAll(SORT_BY_TITLE);

        GetRecipesResponse expected = GetRecipesResponse.builder()
                .recipes(createTestRecipeResponses())
                .build();
        assertEquals(expected, response);
    }

    private static List<GetRecipesResponse.Recipe> createTestRecipeResponses() {
        return List.of(
                GetRecipesResponse.Recipe.builder().id(1L).title("Pizza X").build(),
                GetRecipesResponse.Recipe.builder().id(2L).title("Pizza Y").build());
    }

    private static List<RecipeEntity> createTestRecipes() {
        return List.of(
                RecipeEntity.builder().id(1L).title("Pizza X").build(),
                RecipeEntity.builder().id(2L).title("Pizza Y").build());
    }
}