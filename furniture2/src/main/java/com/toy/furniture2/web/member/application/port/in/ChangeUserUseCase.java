package com.toy.furniture2.web.member.application.port.in;


import com.toy.furniture2.web.member.adapter.in.form.UserBusinessForm;
import com.toy.furniture2.web.member.adapter.in.form.UserForm;

public interface ChangeUserUseCase {
    void insert(UserForm user);
    void insert(UserBusinessForm user);
    void leavUserLogin(String userId); //휴면유저 로그인시
    int updateLoginFailEvent(String userId); //로그인 실패 시
    void updateLoginSuccess(String userId); //로그인 성공 시
}
