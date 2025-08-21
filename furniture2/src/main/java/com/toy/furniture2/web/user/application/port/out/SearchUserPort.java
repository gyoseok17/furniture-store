package com.toy.furniture2.web.user.application.port.out;

import com.toy.furniture2.web.user.domain.SearchUserDto;

public interface SearchUserPort {

    SearchUserDto findByUserId(String userId); //UserDetailsService    
    int countByUserId(String userId); //중복 검사

}
