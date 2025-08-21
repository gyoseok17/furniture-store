package com.toy.furniture2.web.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toy.furniture2.web.user.domain.CustomUserDetails;
import com.toy.furniture2.web.user.application.port.in.SearchUserUseCase;
import com.toy.furniture2.web.user.application.port.out.SearchUserPort;

@RequiredArgsConstructor
@Slf4j
@Service
public class SearchUserService implements SearchUserUseCase, UserDetailsService {

    private final SearchUserPort searchUserPort;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return new CustomUserDetails(searchUserPort.findByUserId(userId));
    }

    @Override
    public int countByUserId(String userId) {
        return searchUserPort.countByUserId(userId);
    }
}
