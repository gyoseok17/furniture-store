package com.toy.furniture2.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toy.furniture2.web.user.domain.BusinessUserVo;

public interface BusinessUserRepository extends JpaRepository<BusinessUserVo, String> {
}
