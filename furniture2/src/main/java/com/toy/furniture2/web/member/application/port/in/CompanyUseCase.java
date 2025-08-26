package com.toy.furniture2.web.member.application.port.in;

import java.util.List;
import java.util.Optional;

import com.toy.furniture2.web.member.domain.CompanyDto;
import com.toy.furniture2.web.member.domain.CompanyVo;

public interface CompanyUseCase {
    
    CompanyVo registerCompany(CompanyVo company);
    
    Optional<CompanyVo> findCompanyByBusinessNumber(String businessNumber);
    
    List<CompanyDto> getAllCompanies();
    
    void deleteCompany(String businessNumber);
    
    CompanyVo updateCompany(CompanyVo company);
}
