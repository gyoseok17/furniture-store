package com.toy.furniture2.web.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.toy.furniture2.web.member.application.port.in.CompanyUseCase;
import com.toy.furniture2.web.member.application.port.out.CompanyPort;
import com.toy.furniture2.web.member.domain.CompanyDto;
import com.toy.furniture2.web.member.domain.CompanyVo;

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
    public Optional<CompanyVo> findCompanyByBusinessNumber(String businessNumber) {
        return companyPort.findByBusinessNumber(businessNumber);
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyPort.findAllCompanies();
    }

    @Override
    public void deleteCompany(String businessNumber) {
        companyPort.deleteByBusinessNumber(businessNumber);
    }

    @Override
    public CompanyVo updateCompany(CompanyVo company) {
        return companyPort.save(company);
    }
}
