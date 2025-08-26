package com.toy.furniture2.web.member.adapter.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import com.toy.furniture2.infrastructure.repositories.BusinessUserRepository;
import com.toy.furniture2.infrastructure.repositories.UserRepository;
import com.toy.furniture2.web.member.application.port.out.ChangeUserPort;
import com.toy.furniture2.web.member.domain.BusinessUserVo;
import com.toy.furniture2.web.member.domain.UserVo;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChangeUserAdapter implements ChangeUserPort {

    private final UserRepository userRepository;
    private final BusinessUserRepository businessUserRepository;


    @Override
    public void insert(BusinessUserVo vo) {
        businessUserRepository.save(vo);
    }

    @Override
    public void insert(UserVo vo) {
        userRepository.save(vo);
    }

    @Override
    public Optional<UserVo> findByUserId(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void flush() {
        businessUserRepository.flush();
        userRepository.flush();
    }
}
