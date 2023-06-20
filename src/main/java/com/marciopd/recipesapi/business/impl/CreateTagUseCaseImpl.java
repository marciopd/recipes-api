package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.CreateTagUseCase;
import com.marciopd.recipesapi.business.exception.DuplicatedTagException;
import com.marciopd.recipesapi.domain.CreateTagRequest;
import com.marciopd.recipesapi.domain.CreateTagResponse;
import com.marciopd.recipesapi.persistence.TagRepository;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTagUseCaseImpl implements CreateTagUseCase {
    private final TagRepository tagRepository;

    @Transactional
    @Override
    public CreateTagResponse createTag(final CreateTagRequest request) {
        String nameLowerCase = StringUtils.lowerCase(request.getName());
        if (tagRepository.existsByName(nameLowerCase)) {
            throw new DuplicatedTagException();
        }

        TagEntity newTag = TagEntity.builder()
                .name(nameLowerCase)
                .build();
        TagEntity savedTag = tagRepository.save(newTag);

        return CreateTagResponse.builder()
                .tagId(savedTag.getId())
                .build();
    }
}
