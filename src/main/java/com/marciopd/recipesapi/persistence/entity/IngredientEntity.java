package com.marciopd.recipesapi.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "recipe_ingredient")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 50)
    @Column(name = "text")
    private String text;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;
}
