package com.toy.furniture2.web.user.application.port.in;

public interface SearchUserUseCase {
    int countByUserId(String userId); //아이디 중복 검사
}
