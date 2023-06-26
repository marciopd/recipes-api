package com.marciopd.recipesapi.controller;

import com.marciopd.recipesapi.business.LoginUseCase;
import com.marciopd.recipesapi.business.exception.InvalidCredentialsException;
import com.marciopd.recipesapi.domain.LoginRequest;
import com.marciopd.recipesapi.domain.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokensControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCaseMock;

    @Test
    void createAuthToken_shouldReturn400_whenCredentialsInvalid() throws Exception {

        LoginRequest expectedRequest = LoginRequest.builder()
                .username("user@notexists.com")
                .password("test123")
                .build();
        when(loginUseCaseMock.login(expectedRequest))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/tokens")
                        .content("""
                                {
                                    "username": "user@notexists.com",
                                    "password": "test123"
                                }
                                """)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        """
                                {
                                    "type":"/validation-error",
                                    "title":"Bad Request",
                                    "status":400,
                                    "detail":"Invalid request",
                                    "instance":"/tokens",
                                    "errors":[{"field":null,"error":"INVALID_CREDENTIALS"}]
                                }
                                """));

        verify(loginUseCaseMock).login(expectedRequest);
    }

    @Test
    void createAuthToken_shouldReturnAccessToken_whenCredentialsOk() throws Exception {

        LoginRequest expectedRequest = LoginRequest.builder()
                .username("user@exists.com")
                .password("test123")
                .build();
        when(loginUseCaseMock.login(expectedRequest))
                .thenReturn(LoginResponse.builder()
                        .accessToken("access_token_encoded")
                        .build());

        mockMvc.perform(post("/tokens")
                        .content("""
                                {
                                    "username": "user@exists.com",
                                    "password": "test123"
                                }
                                """)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        """
                                {
                                    "accessToken":"access_token_encoded"
                                }
                                """));

        verify(loginUseCaseMock).login(expectedRequest);
    }
}