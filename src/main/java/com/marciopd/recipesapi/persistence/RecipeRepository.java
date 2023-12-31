package com.marciopd.recipesapi.persistence;

import com.marciopd.recipesapi.persistence.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

    boolean existsByTitleIgnoreCase(String title);

    interface Spec {
        static Specification<RecipeEntity> containsInstruction(String instruction) {
            return (root, query, criteriaBuilder) -> criteriaBuilder
                    .like(
                            criteriaBuilder.lower(root.get(RecipeEntity_.instructions)),
                            "%" + instruction.toLowerCase().trim() + "%");
        }

        static Specification<RecipeEntity> isNumberServingsEqualsTo(int numberServings) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(
                            root.get(RecipeEntity_.numberServings),
                            numberServings);
        }

        static Specification<RecipeEntity> withIngredient(String ingredient) {
            return (root, query, criteriaBuilder) -> getWithIngredientPredicate(ingredient, root, query, criteriaBuilder);
        }

        static Specification<RecipeEntity> withoutIngredient(String ingredient) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.not(
                            getWithIngredientPredicate(ingredient, root, query, criteriaBuilder));
        }

        static Specification<RecipeEntity> withTag(long tagId) {
            return (root, query, criteriaBuilder) -> getWithTagPredicate(tagId, root, query, criteriaBuilder);
        }

        static Specification<RecipeEntity> withoutTag(long tagId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.not(
                    getWithTagPredicate(tagId, root, query, criteriaBuilder));
        }

        private static Predicate getWithIngredientPredicate(String ingredient,
                                                            Root<RecipeEntity> root,
                                                            CriteriaQuery<?> query,
                                                            CriteriaBuilder criteriaBuilder) {
            Subquery<Integer> existsIngredientSubQuery = query.subquery(Integer.class);
            Root<IngredientEntity> ingredientRoot = existsIngredientSubQuery.from(IngredientEntity.class);
            existsIngredientSubQuery
                    .select(criteriaBuilder.literal(1))
                    .where(
                            criteriaBuilder.equal(ingredientRoot.get(IngredientEntity_.recipe), root),
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(ingredientRoot.get(IngredientEntity_.text)),
                                    "%" + ingredient.toLowerCase().trim() + "%")
                    );
            return criteriaBuilder.exists(existsIngredientSubQuery);
        }

        private static Predicate getWithTagPredicate(long tagId,
                                                     Root<RecipeEntity> root,
                                                     CriteriaQuery<?> query,
                                                     CriteriaBuilder criteriaBuilder) {
            Subquery<Integer> existsTagSubQuery = query.subquery(Integer.class);
            Root<RecipeTagEntity> recipeTagRoot = existsTagSubQuery.from(RecipeTagEntity.class);
            existsTagSubQuery
                    .select(criteriaBuilder.literal(1))
                    .where(
                            criteriaBuilder.equal(recipeTagRoot.get(RecipeTagEntity_.recipe), root),
                            criteriaBuilder.equal(
                                    recipeTagRoot.get(RecipeTagEntity_.tag).get(TagEntity_.id),
                                    tagId)
                    );
            return criteriaBuilder.exists(existsTagSubQuery);
        }
    }
}
