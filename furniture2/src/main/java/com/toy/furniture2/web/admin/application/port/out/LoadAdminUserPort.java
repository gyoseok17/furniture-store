package com.toy.furniture2.web.admin.application.port.out;

import java.util.List;

import com.toy.furniture2.web.member.domain.SearchBusinessUserDto;
import com.toy.furniture2.web.member.domain.SearchUserDto;

public interface LoadAdminUserPort {
    List<SearchUserDto> findGeneralUsers();
    List<SearchBusinessUserDto> findBusinessUsers();
} 