package com.marciopd.recipesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetTagsResponse {
    private List<TagResponse> tags;
}
