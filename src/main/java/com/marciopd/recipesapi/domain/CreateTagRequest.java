package com.marciopd.recipesapi.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateTagRequest {
    @NotBlank
    @Length(min = 3, max = 10)
    private String name;
}
