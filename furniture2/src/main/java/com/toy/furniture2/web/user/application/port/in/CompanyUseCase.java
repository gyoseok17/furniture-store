package com.toy.furniture2.web.user.application.port.in;

import com.toy.furniture2.web.user.domain.CompanyDto;
import com.toy.furniture2.web.user.domain.CompanyVo;

import java.util.List;
import java.util.Optional;

public interface CompanyUseCase {
    
    CompanyVo registerCompany(CompanyVo company);
    
    Optional<CompanyVo> findCompanyById(Integer companyId);
    
    List<CompanyDto> getAllCompanies();
    
    void deleteCompany(Integer companyId);
    
    CompanyVo updateCompany(CompanyVo company);
}
