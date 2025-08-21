package com.toy.furniture2.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toy.furniture2.web.user.domain.UserVo;

public interface UserRepository extends JpaRepository<UserVo, String> {
}
