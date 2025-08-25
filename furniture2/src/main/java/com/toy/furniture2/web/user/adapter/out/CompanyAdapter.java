package com.toy.furniture2.web.user.adapter.out;

import com.toy.furniture2.web.user.application.port.out.CompanyPort;
import com.toy.furniture2.web.user.domain.CompanyDto;
import com.toy.furniture2.web.user.domain.CompanyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public Optional<CompanyVo> findById(Integer companyId) {
        return companyRepository.findById(companyId);
    }

    @Override
    public List<CompanyDto> findAllCompanies() {
        // JPA를 사용하여 회사 목록 조회
        return companyRepository.findAll()
                .stream()
                .map(company -> CompanyDto.builder()
                        .companyId(company.getCompanyId())
                        .companyName(company.getCompanyName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer companyId) {
        companyRepository.deleteById(companyId);
    }
}
