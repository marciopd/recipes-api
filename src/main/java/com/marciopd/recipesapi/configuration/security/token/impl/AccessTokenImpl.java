package com.marciopd.recipesapi.configuration.security.token.impl;

import com.marciopd.recipesapi.configuration.security.token.AccessToken;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
public class AccessTokenImpl implements AccessToken {
    private final Long userId;
    private final String subject;
    private final Set<String> roles;

    public AccessTokenImpl(Long userId, String subject, Collection<String> roles) {
        this.userId = userId;
        this.subject = subject;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}
