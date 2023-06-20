package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.exception.InvalidTagIdsException;
import com.marciopd.recipesapi.domain.TagResponse;
import com.marciopd.recipesapi.persistence.TagRepository;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagEntityConverterImplTest {
    @Mock
    private TagRepository tagRepositoryMock;
    @InjectMocks
    private TagEntityConverterImpl tagEntityConverter;

    @Test
    void toEntity_shouldReturnEmpty_whenTagsNull() {
        List<TagEntity> tagEntities = tagEntityConverter.toEntity(null);
        assertThat(tagEntities).isEmpty();
    }

    @Test
    void toEntity_shouldThrowValidationError_whenInvalidTagInformed() {
        long validId = 1L;
        long invalidId = 2L;
        Set<Long> tagsIds = Set.of(validId, invalidId);

        when(tagRepositoryMock.findByIdIsIn(tagsIds))
                .thenReturn(Set.of(TagEntity.builder().id(validId).build()));

        assertThrows(InvalidTagIdsException.class,
                () -> tagEntityConverter.toEntity(tagsIds));

        verify(tagRepositoryMock).findByIdIsIn(tagsIds);
    }

    @Test
    void toEntity_shouldConvertAllFields_whenValidTagsInformed() {
        Set<Long> tagsIds = Set.of(1L);

        TagEntity expectedTag = TagEntity.builder().id(1L).name("Vegetarian").build();
        when(tagRepositoryMock.findByIdIsIn(tagsIds))
                .thenReturn(Set.of(expectedTag));

        List<TagEntity> tagEntities = tagEntityConverter.toEntity(tagsIds);

        assertEquals(List.of(expectedTag), tagEntities);

        verify(tagRepositoryMock).findByIdIsIn(tagsIds);
    }

    @Test
    void toResponse_shouldReturnEmpty_whenTagsNull() {
        List<TagResponse> tags = tagEntityConverter.toResponse(null);
        assertThat(tags).isEmpty();
    }

    @Test
    void toResponse_shouldReturnConvertedTags_whenTagsInformed() {
        List<TagResponse> tags = tagEntityConverter.toResponse(
                List.of(TagEntity.builder().id(1L).name("Spicy").build()));
        assertEquals(List.of(TagResponse.builder().id(1L).name("Spicy").build()), tags);
    }
}