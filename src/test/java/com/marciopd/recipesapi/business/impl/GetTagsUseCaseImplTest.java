package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.TagEntityConverter;
import com.marciopd.recipesapi.domain.GetTagsResponse;
import com.marciopd.recipesapi.domain.TagResponse;
import com.marciopd.recipesapi.persistence.TagRepository;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTagsUseCaseImplTest {
    @Mock
    private TagRepository tagRepositoryMock;
    @Mock
    private TagEntityConverter tagEntityConverter;
    @InjectMocks
    private GetTagsUseCaseImpl getTagsUseCase;

    @Test
    void getTags_shouldFindAndReturnAll() {
        List<TagEntity> tagEntities = List.of(
                TagEntity.builder().id(1L).name("Cheesy").build());
        when(tagRepositoryMock.findByOrderByNameAsc())
                .thenReturn(tagEntities);

        List<TagResponse> expectedTags = List.of(TagResponse.builder().id(1L).name("Cheesy").build());
        when(tagEntityConverter.toResponse(tagEntities)).thenReturn(
                expectedTags);

        GetTagsResponse response = getTagsUseCase.getTags();

        GetTagsResponse expectedResponse = GetTagsResponse.builder()
                .tags(expectedTags)
                .build();
        assertEquals(expectedResponse, response);

        verify(tagRepositoryMock).findByOrderByNameAsc();
    }
}