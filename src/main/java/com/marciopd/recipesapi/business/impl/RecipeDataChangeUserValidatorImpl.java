package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.RecipeDataChangeUserValidator;
import com.marciopd.recipesapi.business.exception.UnauthorizedDataAccessException;
import com.marciopd.recipesapi.configuration.security.token.AccessToken;
import com.marciopd.recipesapi.persistence.entity.RecipeEntity;
import com.marciopd.recipesapi.persistence.entity.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeDataChangeUserValidatorImpl implements RecipeDataChangeUserValidator {
    private final AccessToken currentRequestUserToken;

    @Override
    public void validateIfCurrentUserCanChangeRecipe(RecipeEntity recipeEntity) {
        if (!currentRequestUserToken.hasRole(RoleEnum.ADMIN.name())
                && !recipeEntity.getUserId().equals(currentRequestUserToken.getUserId())) {
            throw new UnauthorizedDataAccessException("LOGGED_USER_NOT_OWNER_RECIPE");
        }
    }
}
