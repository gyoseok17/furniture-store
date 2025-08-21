package com.toy.furniture2.web.admin.application.port.out;

import com.toy.furniture2.web.user.domain.SearchBusinessUserDto;
import com.toy.furniture2.web.user.domain.SearchUserDto;

import java.util.List;

public interface LoadAdminUserPort {
    List<SearchUserDto> findGeneralUsers();
    List<SearchBusinessUserDto> findBusinessUsers();
} 