package com.marciopd.recipesapi.persistence;

import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.marciopd.recipesapi.persistence.RecipeRepository.Spec.*;
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
        assertTrue(recipeRepository.existsByTitleIgnoreCase("my special dish"));
    }

    @Test
    void existsByTitle_shouldReturnFalse_whenTitleExists() {
        createTestRecipe();
        assertFalse(recipeRepository.existsByTitleIgnoreCase("My special dish does not exist"));
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenServingsSpecIsInformedWithCorrectNumber() {
        RecipeEntity testRecipe = createTestRecipe();

        List<RecipeEntity> results = recipeRepository.findAll(isNumberServingsEqualsTo(2));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenServingsSpecIsInformedNoMatch() {
        createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(isNumberServingsEqualsTo(10));
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenContainsInstructionIsMatch() {
        RecipeEntity testRecipe = createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(containsInstruction("OVEN"));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenContainsInstructionNoMatch() {
        createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(containsInstruction("microwave"));
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenWithIngredientIsMatch() {
        RecipeEntity testRecipe = createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(withIngredient("ingred"));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenWithIngredientNoMatch() {
        createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(withIngredient("cheese"));
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_shouldReturnTestRecipe_whenWithTagIsMatch() {
        RecipeEntity testRecipe = createTestRecipe();
        TagEntity tag = testRecipe.getTags().stream().findFirst().orElseThrow();
        List<RecipeEntity> results = recipeRepository.findAll(withTag(tag.getId()));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnNoResults_whenNoMatch() {
        RecipeEntity testRecipe = createTestRecipe();
        TagEntity vegetarian = entityManager.find(TagEntity.class, 1L);
        List<RecipeEntity> results = recipeRepository.findAll(withoutTag(vegetarian.getId()));
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldReturnResults_whenMatch_combiningSpecifications() {
        RecipeEntity testRecipe = createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(
                Specification.allOf(
                        withTag(testRecipe.getTags().get(0).getId()),
                        withoutTag(1L),
                        withIngredient("01"),
                        withIngredient("02"),
                        withoutIngredient("chicken"),
                        isNumberServingsEqualsTo(2),
                        containsInstruction("OVEN")
                )
        );
        assertEquals(List.of(testRecipe), results);
    }

    @Test
    void findAll_shouldNoReturnResults_whenNoMatch_combiningSpecifications() {
        RecipeEntity testRecipe = createTestRecipe();
        List<RecipeEntity> results = recipeRepository.findAll(
                Specification.allOf(
                        withTag(testRecipe.getTags().get(0).getId()),
                        withoutTag(1L),
                        withIngredient("Cheese"),
                        withIngredient("02"),
                        isNumberServingsEqualsTo(2),
                        containsInstruction("OVEN")
                )
        );
        assertTrue(results.isEmpty());
    }

    private RecipeEntity createTestRecipe() {
        UserEntity userEntity = UserEntity.builder().username("user01").password("test").build();
        entityManager.persist(userEntity);

        TagEntity italian = TagEntity.builder().name("Italian").build();
        entityManager.persist(italian);

        TagEntity pasta = TagEntity.builder().name("Pasta").build();
        entityManager.persist(pasta);

        RecipeEntity recipeEntity = RecipeEntity.builder()
                .user(userEntity)
                .title("My special dish")
                .shortDescription("Short description")
                .numberServings(2)
                .instructions("Very lengthy instructions for this delicious dish. Prepare in the oven.")
                .tags(List.of(italian, pasta))
                .build();
        recipeEntity.addIngredients(List.of("Ingred 01", "Ingred 02", "Ingred 03"));

        entityManager.persist(recipeEntity);

        return recipeEntity;
    }
}