package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TagResponse {
    private Long id;
    private String name;
}
