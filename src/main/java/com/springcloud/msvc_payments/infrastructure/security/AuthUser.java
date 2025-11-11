package com.springcloud.msvc_payments.infrastructure.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser implements UserDetails {

    private final Long id;
    @Getter
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(String idHeader, String emailHeader, String rolesHeader) {
        this.id = Long.parseLong(idHeader);
        this.email = emailHeader;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rolesHeader.toUpperCase()));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}