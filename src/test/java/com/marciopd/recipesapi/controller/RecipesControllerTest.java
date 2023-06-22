package com.marciopd.recipesapi.controller;

import com.marciopd.recipesapi.business.*;
import com.marciopd.recipesapi.business.exception.RecipeNotFoundException;
import com.marciopd.recipesapi.configuration.security.token.AccessTokenEncoder;
import com.marciopd.recipesapi.configuration.security.token.impl.AccessTokenImpl;
import com.marciopd.recipesapi.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccessTokenEncoder accessTokenEncoder;

    @MockBean
    private GetRecipeUseCase getRecipeUseCaseMock;

    @MockBean
    private GetRecipesUseCase getRecipesUseCaseMock;

    @MockBean
    private DeleteRecipeUseCase deleteRecipeUseCaseMock;

    @MockBean
    private CreateRecipeUseCase createRecipeUseCaseMock;

    @MockBean
    private UpdateRecipeUseCase updateRecipeUseCaseMock;

    @Test
    void createRecipe_shouldReturn401_whenNoAuthentication() throws Exception {
        mockMvc.perform(post("/recipes")
                        .content("""
                                {
                                    "title": "American Salad",
                                    "shortDescription": "Prepared with love.",
                                    "instructions": "Many instructions, keep going :D.",
                                    "numberServings": 3,
                                    "ingredients": [
                                        "5 tomatoes",
                                        "20 olive oil",
                                        "1 lettuce"
                                    ],
                                    "tagIds": [
                                        1, 2
                                    ]
                                }
                                """)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(createRecipeUseCaseMock);
    }

    @Test
    void createRecipe_shouldCreateRecipe_whenUserAuthenticated() throws Exception {
        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .numberServings(3)
                .title("American Salad")
                .instructions("Many instructions, keep going :D.")
                .shortDescription("Prepared with love.")
                .tagIds(Set.of(1L, 2L))
                .ingredients(List.of("5 tomatoes", "20 olive oil", "1 lettuce"))
                .build();
        when(createRecipeUseCaseMock.createRecipe(request))
                .thenReturn(CreateRecipeResponse.builder().recipeId(25L).build());

        mockMvc.perform(post("/recipes")
                        .content("""
                                {
                                    "title": "American Salad",
                                    "shortDescription": "Prepared with love.",
                                    "instructions": "Many instructions, keep going :D.",
                                    "numberServings": 3,
                                    "ingredients": [
                                        "5 tomatoes",
                                        "20 olive oil",
                                        "1 lettuce"
                                    ],
                                    "tagIds": [
                                        1, 2
                                    ]
                                }
                                """)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + getAccessToken(10L, "CUSTOMER")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                        "recipeId": 25
                        }
                        """));

        verify(createRecipeUseCaseMock).createRecipe(request);
    }

    @Test
    void updateRecipe_shouldReturn401_whenNoAuthentication() throws Exception {
        mockMvc.perform(put("/recipes/10")
                        .content("""
                                {
                                    "title": "American Salad",
                                    "shortDescription": "Prepared with love.",
                                    "instructions": "Many instructions, keep going :D.",
                                    "numberServings": 3,
                                    "ingredients": [
                                        "5 tomatoes",
                                        "20 olive oil",
                                        "1 lettuce"
                                    ],
                                    "tagIds": [
                                        1, 2
                                    ]
                                }
                                """)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(updateRecipeUseCaseMock);
    }

    @Test
    void updateRecipe_shouldReturn400_whenInvalidInput() throws Exception {
        mockMvc.perform(put("/recipes/10")
                        .header("Authorization", "Bearer " + getAccessToken(10L, "CUSTOMER"))
                        .content("""
                                {
                                }
                                """)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                            "type":"/validation-error",
                            "title":"Bad Request",
                            "status":400,
                            "detail":"Invalid request",
                            "instance":"/recipes/10",
                            "errors":[
                                {"field":"title","error":"must not be blank"},
                                {"field":"numberServings","error":"must not be null"},
                                {"field":"shortDescription","error":"must not be blank"},
                                {"field":"instructions","error":"must not be blank"}
                            ]
                        }
                        """));

        verifyNoInteractions(updateRecipeUseCaseMock);
    }

    @Test
    void updateRecipe_shouldUpdate_whenValidInput() throws Exception {
        mockMvc.perform(put("/recipes/10")
                        .header("Authorization", "Bearer " + getAccessToken(10L, "CUSTOMER"))
                        .content("""
                                {
                                  "title": "Vegetarian dish 2",
                                  "shortDescription": "Short description come here...",
                                  "instructions": "A detailed instructions text comes here...",
                                  "numberServings": 4,
                                  "ingredients": [
                                    "12 tomatoes",
                                    "100ml olive oil",
                                    "2 grs salt"
                                  ],
                                  "tagIds": [
                                    1
                                  ]
                                }
                                                                """)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());

        UpdateRecipeRequest expectedRequest = UpdateRecipeRequest.builder()
                .title("Vegetarian dish 2")
                .shortDescription("Short description come here...")
                .instructions("A detailed instructions text comes here...")
                .numberServings(4)
                .ingredients(List.of("12 tomatoes", "100ml olive oil", "2 grs salt"))
                .tagIds(Set.of(1L))
                .build();
        verify(updateRecipeUseCaseMock).updateRecipe(10L, expectedRequest);
    }

    @Test
    void getRecipe_shouldReturnAllFields_whenFound() throws Exception {
        GetRecipeResponse getRecipeResponse = GetRecipeResponse.builder()
                .id(10L)
                .numberServings(5)
                .title("Lasagne Supreme")
                .user(GetRecipeResponse.User.builder().id(10L).username("maverick").build())
                .instructions("Follow the instructions carefully.")
                .shortDescription("Fast and good recipe!")
                .creationTime(Instant.parse("2007-12-03T10:15:30.00Z"))
                .tags(List.of(TagResponse.builder().id(1L).name("vegetarian").build()))
                .ingredients(List.of(GetRecipeResponse.Ingredient.builder().text("1Kg Pasta").build()))
                .build();
        when(getRecipeUseCaseMock.getRecipe(10L)).thenReturn(getRecipeResponse);

        mockMvc.perform(get("/recipes/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                        "id":10,
                        "user": {"id": 10, "username": "maverick"},
                        "title":"Lasagne Supreme",
                        "shortDescription":"Fast and good recipe!",
                        "instructions":"Follow the instructions carefully.",
                        "numberServings":5,
                        "creationTime":"2007-12-03T10:15:30Z",
                        "ingredients":[{"text":"1Kg Pasta"}],
                        "tags":[{"id":1,"name":"vegetarian"}]
                        }
                        """));

        verify(getRecipeUseCaseMock).getRecipe(10L);
    }

    @Test
    void getRecipe_shouldReturn404_whenNotFound() throws Exception {
        when(getRecipeUseCaseMock.getRecipe(33L)).thenThrow(new RecipeNotFoundException());

        mockMvc.perform(get("/recipes/33"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(content().json("""
                        {
                        "type":"/validation-error",
                        "title":"Not Found",
                        "status":404,
                        "detail":"Invalid request",
                        "instance":"/recipes/33",
                        "errors":[]
                        }
                        """));

        verify(getRecipeUseCaseMock).getRecipe(33L);
    }

    @Test
    void getRecipes_shouldReturnAllFields_whenResults() throws Exception {
        GetRecipesResponse getRecipesResponse = GetRecipesResponse.builder()
                .recipes(List.of(
                        GetRecipesResponse.Recipe.builder().id(1L).title("Special Pasta Dish I").build(),
                        GetRecipesResponse.Recipe.builder().id(2L).title("Special Pasta Dish II").build()))
                .build();

        GetRecipesRequest request = GetRecipesRequest.builder()
                .withIngredients(List.of("potatoes", "pasta"))
                .withoutIngredients(List.of("pepper"))
                .numberServings(4)
                .withTags(List.of(1L))
                .withoutTags(List.of(2L))
                .instruction("oven")
                .build();
        when(getRecipesUseCaseMock.getRecipes(request)).thenReturn(getRecipesResponse);

        mockMvc.perform(get("/recipes?with=potatoes&with=pasta" +
                        "&without=pepper&numberServings=4" +
                        "&withTag=1&withoutTag=2&instruction=oven"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(
                        """
                                    {
                                        "recipes":[
                                            {"id":1,"title":"Special Pasta Dish I"},
                                            {"id":2,"title":"Special Pasta Dish II"}
                                        ]
                                    }
                                """));

        verify(getRecipesUseCaseMock).getRecipes(request);
    }

    @Test
    void getRecipes_shouldReturnEmpty_whenNoResults() throws Exception {
        GetRecipesResponse getRecipesResponse = GetRecipesResponse.builder()
                .build();

        GetRecipesRequest request = GetRecipesRequest.builder()
                .withTags(List.of(1L, 2L))
                .instruction("cook")
                .build();
        when(getRecipesUseCaseMock.getRecipes(request)).thenReturn(getRecipesResponse);

        mockMvc.perform(get("/recipes?withTag=1&withTag=2&instruction=cook"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(
                        """
                                    {
                                        "recipes":[]
                                    }
                                """));

        verify(getRecipesUseCaseMock).getRecipes(request);
    }

    @Test
    void deleteRecipe_shouldReturn401_whenNoAuthentication() throws Exception {

        mockMvc.perform(delete("/recipes/201"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(deleteRecipeUseCaseMock);
    }

    @Test
    void deleteRecipe_shouldCallDelete_whenUserRoleCustomer() throws Exception {

        mockMvc.perform(delete("/recipes/201")
                        .header("Authorization", "Bearer " + getAccessToken(50L, "CUSTOMER")))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteRecipeUseCaseMock).deleteRecipe(201L);
    }

    @Test
    void deleteRecipe_shouldCallDelete_whenUserAdmin() throws Exception {

        mockMvc.perform(delete("/recipes/210")
                        .header("Authorization", "Bearer " + getAccessToken(100L, "ADMIN")))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteRecipeUseCaseMock).deleteRecipe(210L);
    }

    private String getAccessToken(Long userId, String role) {
        return accessTokenEncoder.encode(new AccessTokenImpl(userId, "subject", List.of(role)));
    }
}