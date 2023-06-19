package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.persistence.entity.TagEntity;

import java.util.List;
import java.util.Set;

public interface TagEntityConverter {
    List<TagEntity> convertToEntity(final Set<Long> tagIds);
}
