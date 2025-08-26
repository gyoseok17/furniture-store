package com.toy.furniture2.web.member.application.port.out;

import com.toy.furniture2.web.member.domain.SearchUserDto;

public interface SearchUserPort {

    SearchUserDto findByUserId(String userId); //UserDetailsService    
    int countByUserId(String userId); //중복 검사

}
