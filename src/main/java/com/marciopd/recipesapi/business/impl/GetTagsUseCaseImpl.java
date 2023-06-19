package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.GetTagsUseCase;
import com.marciopd.recipesapi.domain.GetTagsResponse;
import com.marciopd.recipesapi.persistence.TagRepository;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTagsUseCaseImpl implements GetTagsUseCase {
    private final TagRepository tagRepository;

    @Transactional
    @Override
    public GetTagsResponse getTags() {
        final List<TagEntity> tags = tagRepository.findByOrderByNameAsc();
        final List<GetTagsResponse.Tag> convertToResponse = tags.stream()
                .map(tagEntity -> GetTagsResponse.Tag.builder()
                        .id(tagEntity.getId())
                        .name(tagEntity.getName())
                        .build())
                .toList();

        return GetTagsResponse.builder()
                .tags(convertToResponse)
                .build();
    }
}
