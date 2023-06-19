package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTagResponse {
    private Long tagId;
}
