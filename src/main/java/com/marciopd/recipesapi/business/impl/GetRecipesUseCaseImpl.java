package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.GetRecipesUseCase;
import com.marciopd.recipesapi.domain.GetRecipesRequest;
import com.marciopd.recipesapi.domain.GetRecipesResponse;
import com.marciopd.recipesapi.persistence.RecipeRepository;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.marciopd.recipesapi.persistence.RecipeRepository.Spec.*;

@Service
@RequiredArgsConstructor
public class GetRecipesUseCaseImpl implements GetRecipesUseCase {
    private static final Sort SORT_BY_TITLE = Sort.by("title");

    private final RecipeRepository recipeRepository;

    @Override
    public GetRecipesResponse getRecipes(final GetRecipesRequest request) {
        List<Specification<RecipeEntity>> specifications = new ArrayList<>();

        addNumberServingsSpecification(request, specifications);
        addInstructionsSpecification(request, specifications);

        addWithIngredientsSpecifications(request, specifications);
        addWithoutIngredientsSpecifications(request, specifications);

        addWithTagsSpecifications(request, specifications);
        addWithoutTagsSpecifications(request, specifications);

        List<RecipeEntity> results;
        if (!specifications.isEmpty()) {
            results = recipeRepository.findAll(Specification.allOf(specifications), SORT_BY_TITLE);
        } else {
            results = recipeRepository.findAll(SORT_BY_TITLE);
        }

        return GetRecipesResponse.builder()
                .recipes(convertToOverview(results))
                .build();
    }

    private void addWithoutTagsSpecifications(GetRecipesRequest request, List<Specification<RecipeEntity>> specifications) {
        if (!CollectionUtils.isEmpty(request.getWithoutTags())) {
            request.getWithoutTags().forEach(tagId -> specifications.add(withoutTag(tagId)));
        }
    }

    private void addWithTagsSpecifications(GetRecipesRequest request, List<Specification<RecipeEntity>> specifications) {
        if (!CollectionUtils.isEmpty(request.getWithTags())) {
            request.getWithTags().forEach(tagId -> specifications.add(withTag(tagId)));
        }
    }

    private void addInstructionsSpecification(GetRecipesRequest request, List<Specification<RecipeEntity>> specifications) {
        if (!StringUtils.isBlank(request.getInstruction())) {
            specifications.add(containsInstruction(request.getInstruction().toLowerCase().trim()));
        }
    }

    private static void addNumberServingsSpecification(GetRecipesRequest request,
                                                       List<Specification<RecipeEntity>> specifications) {
        if (request.getNumberServings() != null) {
            specifications.add(isNumberServingsEqualsTo(request.getNumberServings()));
        }
    }

    private static void addWithoutIngredientsSpecifications(GetRecipesRequest request,
                                                            List<Specification<RecipeEntity>> specifications) {
        if (!CollectionUtils.isEmpty(request.getWithoutIngredients())) {
            request.getWithoutIngredients().forEach(ingredient -> specifications.add(withoutIngredient(ingredient)));
        }
    }

    private static void addWithIngredientsSpecifications(GetRecipesRequest request,
                                                         List<Specification<RecipeEntity>> specifications) {
        if (!CollectionUtils.isEmpty(request.getWithIngredients())) {
            request.getWithIngredients().forEach(ingredient -> specifications.add(withIngredient(ingredient)));
        }
    }

    private static List<GetRecipesResponse.Recipe> convertToOverview(List<RecipeEntity> results) {
        return results.stream()
                .map(recipeEntity -> GetRecipesResponse.Recipe.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .build())
                .toList();
    }
}
