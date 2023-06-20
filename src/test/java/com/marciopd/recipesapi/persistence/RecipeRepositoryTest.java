package com.marciopd.recipesapi.persistence;

import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void existsByTitle_shouldReturnTrue_whenTitleExists() {
        createTestRecipe();
        assertTrue(recipeRepository.existsByTitle("My special dish"));
    }

    @Test
    void existsByTitle_shouldReturnFalse_whenTitleExists() {
        createTestRecipe();
        assertFalse(recipeRepository.existsByTitle("My special dish does not exist"));
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenServingsSpecIsInformedWithCorrectNumber() {
        RecipeEntity testRecipe = createTestRecipe();

        List<RecipeEntity> results = recipeRepository.findAll(RecipeRepository.Spec.isNumberServingsEqualsTo(2));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenServingsSpecIsInformedNoMatch() {
        createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(RecipeRepository.Spec.isNumberServingsEqualsTo(10));
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenContainsInstructionIsMatch() {
        RecipeEntity testRecipe = createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(RecipeRepository.Spec.containsInstruction("oven"));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenContainsInstructionNoMatch() {
        createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(RecipeRepository.Spec.containsInstruction("microwave"));
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenWithIngredientIsMatch() {
        RecipeEntity testRecipe = createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(RecipeRepository.Spec.withIngredient("01"));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenWithIngredientNoMatch() {
        createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(RecipeRepository.Spec.withIngredient("cheese"));
        assertTrue(results.isEmpty());
    }

    private RecipeEntity createTestRecipe() {
        UserEntity userEntity = UserEntity.builder().username("user01").password("test").build();
        entityManager.persist(userEntity);

        TagEntity italian = TagEntity.builder().name("Italian").build();
        entityManager.persist(italian);

        RecipeEntity recipeEntity = RecipeEntity.builder()
                .user(userEntity)
                .title("My special dish")
                .shortDescription("Short description")
                .numberServings(2)
                .instructions("Very lengthy instructions for this delicious dish. Prepare in the oven.")
                .tags(List.of(italian))
                .build();
        recipeEntity.addIngredients(List.of("Ingred 01", "Ingred 02", "Ingred 03"));

        entityManager.persist(recipeEntity);

        return recipeEntity;
    }
}