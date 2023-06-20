package com.marciopd.recipesapi.controller;

import com.marciopd.recipesapi.business.CreateTagUseCase;
import com.marciopd.recipesapi.business.GetTagsUseCase;
import com.marciopd.recipesapi.domain.CreateTagRequest;
import com.marciopd.recipesapi.domain.CreateTagResponse;
import com.marciopd.recipesapi.domain.GetTagsResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagsController {

    private final CreateTagUseCase createTagUseCase;
    private final GetTagsUseCase getTagsUseCase;

    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<CreateTagResponse> createTag(@Valid @RequestBody final CreateTagRequest request) {
        final CreateTagResponse response = createTagUseCase.createTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetTagsResponse> getTags() {
        return ResponseEntity.ok(getTagsUseCase.getTags());
    }
}
