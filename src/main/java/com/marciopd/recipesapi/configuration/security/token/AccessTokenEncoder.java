package com.marciopd.recipesapi.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
