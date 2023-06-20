package com.marciopd.recipesapi.business.impl;

import com.marciopd.recipesapi.business.LoginUseCase;
import com.marciopd.recipesapi.business.exception.InvalidCredentialsException;
import com.marciopd.recipesapi.configuration.security.token.AccessTokenEncoder;
import com.marciopd.recipesapi.configuration.security.token.impl.AccessTokenImpl;
import com.marciopd.recipesapi.domain.LoginRequest;
import com.marciopd.recipesapi.domain.LoginResponse;
import com.marciopd.recipesapi.persistence.UserRepository;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Set<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .collect(Collectors.toSet());

        return accessTokenEncoder.encode(new AccessTokenImpl(user.getId(), user.getUsername(), roles));
    }

}
