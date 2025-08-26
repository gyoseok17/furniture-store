package com.toy.furniture2.web.member.application.port.out;

import java.util.Optional;

import com.toy.furniture2.web.member.domain.BusinessUserVo;
import com.toy.furniture2.web.member.domain.UserVo;

public interface ChangeUserPort {

    void insert(BusinessUserVo vo);
    void insert(UserVo vo);
    Optional<UserVo> findByUserId(String userId);
    void flush();

}
