package com.marciopd.recipesapi.persistence;

import com.marciopd.recipesapi.persistence.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
}
