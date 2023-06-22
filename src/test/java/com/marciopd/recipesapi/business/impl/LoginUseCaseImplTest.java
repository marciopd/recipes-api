package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.exception.InvalidCredentialsException;
import com.marciopd.recipesapi.configuration.security.token.AccessToken;
import com.marciopd.recipesapi.configuration.security.token.AccessTokenEncoder;
import com.marciopd.recipesapi.configuration.security.token.impl.AccessTokenImpl;
import com.marciopd.recipesapi.domain.LoginRequest;
import com.marciopd.recipesapi.domain.LoginResponse;
import com.marciopd.recipesapi.persistence.UserRepository;
import com.marciopd.recipesapi.persistence.entity.RoleEnum;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import com.marciopd.recipesapi.persistence.entity.UserRoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private AccessTokenEncoder accessTokenEncoderMock;
    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void login_shouldThrowInvalidCredentialsException_whenUsernameInvalid() {
        String username = "james@gmail.com";
        LoginRequest request = LoginRequest.builder().username(username).build();

        when(userRepositoryMock.findByUsername(username)).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(request));

        verify(userRepositoryMock).findByUsername(username);
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordInvalid() {
        String username = "james@gmail.com";
        String password = "password";
        LoginRequest request = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        String hashedPassword = "hashed";
        UserEntity userEntity = UserEntity.builder().id(1L).password(hashedPassword).build();
        when(userRepositoryMock.findByUsername(username)).thenReturn(userEntity);

        when(passwordEncoderMock.matches(password, hashedPassword))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(request));

        verify(userRepositoryMock).findByUsername(username);
        verify(passwordEncoderMock).matches(password, hashedPassword);
    }

    @Test
    void login_shouldReturnToken_whenValidCredentials() {
        String username = "james@gmail.com";
        String password = "password";
        LoginRequest request = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        String hashedPassword = "hashed";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .password(hashedPassword)
                .userRoles(Set.of(
                        UserRoleEntity.builder().role(RoleEnum.ADMIN).build(),
                        UserRoleEntity.builder().role(RoleEnum.CUSTOMER).build()))
                .build();
        when(userRepositoryMock.findByUsername(username)).thenReturn(userEntity);

        when(passwordEncoderMock.matches(password, hashedPassword))
                .thenReturn(true);

        AccessToken expectedToken = new AccessTokenImpl(1L, username, Set.of("ADMIN", "CUSTOMER"));
        String encodedToken = "ABCTOKEN";
        when(accessTokenEncoderMock.encode(expectedToken)).thenReturn(encodedToken);

        LoginResponse response = loginUseCase.login(request);

        verify(userRepositoryMock).findByUsername(username);
        verify(passwordEncoderMock).matches(password, hashedPassword);
        verify(accessTokenEncoderMock).encode(expectedToken);

        LoginResponse expected = LoginResponse.builder()
                .accessToken(encodedToken)
                .build();
        assertEquals(expected, response);
    }
}