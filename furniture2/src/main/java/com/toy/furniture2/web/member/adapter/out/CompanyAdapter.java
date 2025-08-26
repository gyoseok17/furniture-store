package com.toy.furniture2.web.member.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.toy.furniture2.web.member.application.port.out.CompanyPort;
import com.toy.furniture2.web.member.domain.CompanyDto;
import com.toy.furniture2.web.member.domain.CompanyVo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompanyAdapter implements CompanyPort {

    private final com.toy.furniture2.infrastructure.repositories.CompanyRepository companyRepository;

    @Override
    public CompanyVo save(CompanyVo company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<CompanyVo> findByBusinessNumber(String businessNumber) {
        return companyRepository.findById(businessNumber);
    }

    @Override
    public List<CompanyDto> findAllCompanies() {
        // JPA를 사용하여 회사 목록 조회
        return companyRepository.findAll()
                .stream()
                .map(company -> CompanyDto.builder()
                        .businessNumber(company.getBusinessNumber())
                        .businessName(company.getBusinessName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByBusinessNumber(String businessNumber) {
        companyRepository.deleteById(businessNumber);
    }
}
