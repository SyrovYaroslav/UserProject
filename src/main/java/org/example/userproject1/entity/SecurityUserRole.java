package org.example.userproject1.entity;

import org.springframework.security.core.GrantedAuthority;


public enum SecurityUserRole implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority(){
        return name();
    }
}
