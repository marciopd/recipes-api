package com.marciopd.recipesapi.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "recipe")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(0)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Length(max = 50)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Length(max = 300)
    @Column(name = "short_description")
    private String shortDescription;

    @NotBlank
    @Length(max = 5000)
    @Column(name = "instructions")
    private String instructions;

    @NotNull
    @Min(1)
    @Max(50)
    @Column(name = "number_servings")
    private Integer numberServings;

    @CreationTimestamp
    @Column(name = "creation_time")
    private Instant creationTime;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OrderBy("name")
    private List<TagEntity> tags;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<IngredientEntity> ingredients;

    public List<TagEntity> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public List<IngredientEntity> getIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        return ingredients;
    }

    public void addIngredients(final Collection<String> ingredients) {
        ingredients.forEach(ingredientText -> {
            final IngredientEntity ingredientEntity = IngredientEntity.builder()
                    .text(ingredientText)
                    .recipe(this)
                    .build();
            getIngredients().add(ingredientEntity);
        });
    }

    public void clearIngredients() {
        this.getIngredients().clear();
    }

    public void clearTags() {
        this.getTags().clear();
    }

    public void addTags(List<TagEntity> tags) {
        this.getTags().addAll(tags);
    }
}
