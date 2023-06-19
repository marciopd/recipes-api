package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.business.exception.InvalidTagIdsException;
import com.marciopd.recipesapi.persistence.TagRepository;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TagEntityConverterImpl implements TagEntityConverter {
    private final TagRepository tagRepository;

    @Override
    public List<TagEntity> convertToEntity(Set<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return Collections.emptyList();
        }

        Set<TagEntity> tagsFound = tagRepository.findByIdIsIn(tagIds);
        validateTagIds(tagIds, tagsFound);

        return tagsFound.stream().toList();
    }

    private static void validateTagIds(Set<Long> tagIds, Set<TagEntity> tagsFound) {
        Set<Long> tagIdsFound = tagsFound.stream().map(tagEntity -> tagEntity.getId()).collect(Collectors.toSet());
        if (!tagIds.equals(tagIdsFound)) {
            log.error("Invalid tag ids. Tags informed: {}; Tags Found: {}", tagIds, tagIdsFound);
            throw new InvalidTagIdsException();
        }
    }
}
