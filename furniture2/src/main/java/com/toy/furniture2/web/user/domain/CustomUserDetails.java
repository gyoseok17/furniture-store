package com.toy.furniture2.web.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final SearchUserDto searchUserDto;

    public CustomUserDetails(final SearchUserDto searchUserDto) {
        this.searchUserDto = searchUserDto;
    }

    public SearchUserDto getSearchUserDto() {
        return searchUserDto;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + searchUserDto.getRole()));
    }

    @Override
    public String getPassword() {
        return searchUserDto.getPwd();
    }

    @Override
    public String getUsername() {
        return searchUserDto.getUserId();
    }
}
