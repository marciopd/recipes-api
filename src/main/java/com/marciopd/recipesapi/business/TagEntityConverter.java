package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.TagResponse;
import com.marciopd.recipesapi.persistence.entity.TagEntity;

import java.util.List;
import java.util.Set;

public interface TagEntityConverter {
    List<TagEntity> toEntity(final Set<Long> tagIds);

    List<TagResponse> toResponse(final List<TagEntity> tags);
}
