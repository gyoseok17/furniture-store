package com.toy.furniture2.web.user.application.service;

import com.toy.furniture2.web.user.application.port.in.CompanyUseCase;
import com.toy.furniture2.web.user.application.port.out.CompanyPort;
import com.toy.furniture2.web.user.domain.CompanyDto;
import com.toy.furniture2.web.user.domain.CompanyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService implements CompanyUseCase {

    private final CompanyPort companyPort;

    @Override
    public CompanyVo registerCompany(CompanyVo company) {
        return companyPort.save(company);
    }

    @Override
    public Optional<CompanyVo> findCompanyById(Integer companyId) {
        return companyPort.findById(companyId);
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyPort.findAllCompanies();
    }

    @Override
    public void deleteCompany(Integer companyId) {
        companyPort.deleteById(companyId);
    }

    @Override
    public CompanyVo updateCompany(CompanyVo company) {
        return companyPort.save(company);
    }
}
