package com.marciopd.recipesapi.controller;

import com.marciopd.recipesapi.business.CreateTagUseCase;
import com.marciopd.recipesapi.business.GetTagsUseCase;
import com.marciopd.recipesapi.domain.CreateTagRequest;
import com.marciopd.recipesapi.domain.CreateTagResponse;
import com.marciopd.recipesapi.domain.GetTagsResponse;
import com.marciopd.recipesapi.domain.TagResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagsControllerTest {

    @Mock
    private CreateTagUseCase createTagUseCaseMock;
    @Mock
    private GetTagsUseCase getTagsUseCaseMock;

    @InjectMocks
    private TagsController tagsController;

    @Test
    void createTag_shouldCallUseCaseAndReturnCreateResponse() {
        CreateTagRequest request = CreateTagRequest.builder()
                .name("Vegetarian")
                .build();

        CreateTagResponse responseBody = CreateTagResponse.builder()
                .tagId(100L)
                .build();
        when(createTagUseCaseMock.createTag(request)).thenReturn(responseBody);

        ResponseEntity<CreateTagResponse> responseEntity = tagsController.createTag(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(responseBody, responseEntity.getBody());

        verify(createTagUseCaseMock).createTag(request);
        verifyNoInteractions(getTagsUseCaseMock);
    }

    @Test
    void getTags_shouldCallUseCaseAndReturnCreateResponse() {
        GetTagsResponse responseBody = GetTagsResponse.builder()
                .tags(List.of(TagResponse.builder().id(1L).name("Spicy").build()))
                .build();
        when(getTagsUseCaseMock.getTags()).thenReturn(responseBody);

        ResponseEntity<GetTagsResponse> responseEntity = tagsController.getTags();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseBody, responseEntity.getBody());

        verify(getTagsUseCaseMock).getTags();
    }
}