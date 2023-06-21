package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.exception.DuplicatedRecipeException;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueRecipeValidatorImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private UniqueRecipeValidatorImpl uniqueRecipeValidator;

    @Test
    void validate_shouldThrowError_whenTitleExists() {
        String title = "Title";
        when(recipeRepository.existsByTitleIgnoreCase(title)).thenReturn(true);

        assertThrows(DuplicatedRecipeException.class,
                () -> uniqueRecipeValidator.validate(title));

        verify(recipeRepository).existsByTitleIgnoreCase(title);
    }

    @Test
    void validate_shouldReturn_whenTitleNew() {
        String newTitle = "New Title";
        when(recipeRepository.existsByTitleIgnoreCase(newTitle)).thenReturn(false);

        uniqueRecipeValidator.validate(newTitle);

        verify(recipeRepository).existsByTitleIgnoreCase(newTitle);
    }
}