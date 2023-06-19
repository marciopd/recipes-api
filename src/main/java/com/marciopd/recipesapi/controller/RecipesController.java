package com.marciopd.recipesapi.controller;

import com.marciopd.recipesapi.business.*;
import com.marciopd.recipesapi.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipesController {

    private final CreateRecipeUseCase createRecipeUseCase;
    private final GetRecipeUseCase getRecipeUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final DeleteRecipeUseCase deleteRecipeUseCase;
    private final GetRecipesUseCase getRecipesUseCase;

    @PostMapping
    public ResponseEntity<CreateRecipeResponse> createRecipe(@Valid @RequestBody CreateRecipeRequest request) {
        CreateRecipeResponse response = createRecipeUseCase.createRecipe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetRecipeResponse> getRecipe(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(getRecipeUseCase.getRecipe(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable @Positive Long id, @Valid @RequestBody UpdateRecipeRequest request) {
        updateRecipeUseCase.updateRecipe(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable @Positive Long id) {
        deleteRecipeUseCase.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<GetRecipesResponse> getRecipes(@RequestParam(required = false) List<String> with,
                                                         @RequestParam(required = false) List<String> without,
                                                         @RequestParam(required = false) Integer numberServings,
                                                         @RequestParam(required = false) List<Long> withTag,
                                                         @RequestParam(required = false) List<Long> withoutTag,
                                                         @RequestParam(required = false) String instruction) {
        GetRecipesResponse response = getRecipesUseCase.getRecipes(GetRecipesRequest.builder()
                .withIngredients(with)
                .withoutIngredients(without)
                .withTags(withTag)
                .withoutTags(withoutTag)
                .numberServings(numberServings)
                .instruction(instruction)
                .build());
        return ResponseEntity.ok(response);
    }
}
