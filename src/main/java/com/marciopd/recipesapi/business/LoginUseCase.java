package com.marciopd.recipesapi.business;

import com.marciopd.recipesapi.domain.LoginRequest;
import com.marciopd.recipesapi.domain.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
