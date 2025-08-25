package com.toy.furniture2.web.user.application.port.out;

import com.toy.furniture2.web.user.domain.CompanyDto;
import com.toy.furniture2.web.user.domain.CompanyVo;

import java.util.List;
import java.util.Optional;

public interface CompanyPort {
    
    CompanyVo save(CompanyVo company);
    
    Optional<CompanyVo> findById(Integer companyId);
    
    List<CompanyDto> findAllCompanies();
    
    void deleteById(Integer companyId);
}
