package com.example.chatapp.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    USER("USER_ROLE"),ADMIN("ADMIN_ROLE");

    private final String authority;

    Role(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
