package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.CreateTagRequest;
import com.marciopd.recipesapi.domain.CreateTagResponse;

public interface CreateTagUseCase {
    CreateTagResponse createTag(CreateTagRequest request);
}
