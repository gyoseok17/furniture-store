package com.toy.furniture2.web.member.application.port.out;

import java.util.List;
import java.util.Optional;

import com.toy.furniture2.web.member.domain.CompanyDto;
import com.toy.furniture2.web.member.domain.CompanyVo;

public interface CompanyPort {
    
    CompanyVo save(CompanyVo company);
    
    Optional<CompanyVo> findByBusinessNumber(String businessNumber);
    
    List<CompanyDto> findAllCompanies();
    
    void deleteByBusinessNumber(String businessNumber);
}
