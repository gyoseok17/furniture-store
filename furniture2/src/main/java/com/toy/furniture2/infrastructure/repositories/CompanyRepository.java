package com.toy.furniture2.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.toy.furniture2.web.user.domain.CompanyVo;

public interface CompanyRepository extends JpaRepository<CompanyVo, Integer> {
}
