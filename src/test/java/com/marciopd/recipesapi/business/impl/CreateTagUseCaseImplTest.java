package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.exception.DuplicatedTagException;
import com.marciopd.recipesapi.domain.CreateTagRequest;
import com.marciopd.recipesapi.domain.CreateTagResponse;
import com.marciopd.recipesapi.persistence.TagRepository;
import com.marciopd.recipesapi.persistence.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTagUseCaseImplTest {
    @Mock
    private TagRepository tagRepositoryMock;

    @InjectMocks
    private CreateTagUseCaseImpl createTagUseCase;

    @Test
    void createTag_shouldThrowDuplicatedError_WhenNameExists() {
        String tagName = "Vegetarian";
        when(tagRepositoryMock.existsByNameIgnoreCase(tagName))
                .thenReturn(true);

        CreateTagRequest request = CreateTagRequest.builder()
                .name(tagName)
                .build();

        assertThrows(DuplicatedTagException.class, () -> createTagUseCase.createTag(request));

        verify(tagRepositoryMock).existsByNameIgnoreCase(tagName);
    }

    @Test
    void createTag_shouldSave_WhenNewName() {
        String tagName = "Brazilian";
        when(tagRepositoryMock.existsByNameIgnoreCase(tagName))
                .thenReturn(false);

        TagEntity expectedTagEntity = TagEntity.builder().name(tagName).build();
        TagEntity savedTagEntity = TagEntity.builder().id(101L).build();
        when(tagRepositoryMock.save(expectedTagEntity))
                .thenReturn(savedTagEntity);

        CreateTagRequest request = CreateTagRequest.builder()
                .name("  Brazilian  ")
                .build();
        CreateTagResponse response = createTagUseCase.createTag(request);

        CreateTagResponse expectedResponse = CreateTagResponse.builder()
                .tagId(101L)
                .build();
        assertEquals(expectedResponse, response);

        verify(tagRepositoryMock).existsByNameIgnoreCase(tagName);
        verify(tagRepositoryMock).save(expectedTagEntity);
    }
}