package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.GetTagsUseCase;
import com.marciopd.recipesapi.business.TagEntityConverter;
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
    private final TagEntityConverter tagEntityConverter;

    @Transactional
    @Override
    public GetTagsResponse getTags() {
        final List<TagEntity> tags = tagRepository.findByOrderByNameAsc();

        return GetTagsResponse.builder()
                .tags(tagEntityConverter.toResponse(tags))
                .build();
    }
}
