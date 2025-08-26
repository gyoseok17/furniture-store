package com.toy.furniture2.web.member.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toy.furniture2.infrastructure.repositories.CompanyRepository;
import com.toy.furniture2.web.member.adapter.in.form.BusinessForm;
import com.toy.furniture2.web.member.adapter.in.form.UserBusinessForm;
import com.toy.furniture2.web.member.adapter.in.form.UserForm;
import com.toy.furniture2.web.member.application.port.in.ChangeUserUseCase;
import com.toy.furniture2.web.member.application.port.out.ChangeUserPort;
import com.toy.furniture2.web.member.domain.BusinessUserVo;
import com.toy.furniture2.web.member.domain.CompanyVo;
import com.toy.furniture2.web.member.domain.UserVo;

import java.time.Instant;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class ChangeUserService implements ChangeUserUseCase {

    private final ChangeUserPort changeUserPort;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    //일반 회원 가입
    @Override
    public void insert(UserForm user) {
        String encode = passwordEncoder.encode(user.getPwd());
        UserVo userVo = UserVo.builder()
                .userId(user.getUserId())
                .pwd(encode)
                .userNm(user.getUserNm())
                .email(user.getEmail())
                .zipcode(user.getZipcode())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .useYn("Y")
                .lockYn("N")
                .leavYn("N")
                .lgnFailCnt(0)
                .lastLgnDtm(Instant.now())
                .role("GENERAL")
                .regDtm(Instant.now())
                .build();

        changeUserPort.insert(userVo);
        changeUserPort.flush();
    }

    //사업자 회원 가입
    @Override
    public void insert(UserBusinessForm user) {
        UserForm userForm = user.getUserForm();
        BusinessForm businessForm = user.getBusinessForm();
        String encode = passwordEncoder.encode(userForm.getPwd());

        UserVo userVo = UserVo.builder()
                .userId(userForm.getUserId())
                .pwd(encode)
                .userNm(userForm.getUserNm())
                .email(userForm.getEmail())
                .zipcode(userForm.getZipcode())
                .address(userForm.getAddress())
                .addressDetail(userForm.getAddressDetail())
                .useYn("Y")
                .lockYn("N")
                .leavYn("N")
                .lgnFailCnt(0)
                .lastLgnDtm(Instant.now())
                .role("BUSINESS")
                .regDtm(Instant.now())
                .build();

        // 1) 회사 등록 (business_number를 PK로 사용)
        CompanyVo company = CompanyVo.builder()
                .businessNumber(businessForm.getBusinessNumber().replaceAll("-", ""))
                .businessName(businessForm.getBusinessName())
                .build();
        company = companyRepository.save(company);

        // 2) 비즈니스 회원 생성 (business_number FK 사용)
        BusinessUserVo businessUserVo = BusinessUserVo.builder()
                .approvedYn("N")
                .userId(userForm.getUserId())
                .businessName(businessForm.getBusinessName())
                .businessNumber(businessForm.getBusinessNumber().replaceAll("-", ""))
                .build();

        changeUserPort.insert(userVo);
        changeUserPort.insert(businessUserVo);
        changeUserPort.flush();

    }

    //휴면 회원 비활성화
    @Override
    public void leavUserLogin(String userId) {
        changeUserPort.findByUserId(userId).ifPresent(user -> {
            user.setLeavYn("N");
        });

        changeUserPort.flush();
    }

    //로그인 실패 이벤트
    @Override
    public int updateLoginFailEvent(String userId) {
        Optional<UserVo> option = changeUserPort.findByUserId(userId);
        if(option.isEmpty()) {
            return 0;
        }

        UserVo userVo = option.get();
        int count = userVo.getLgnFailCnt() + 1;
        userVo.setLgnFailCnt(count);

        if (count >= 5) {
            userVo.setLockYn("Y");
        }

        changeUserPort.flush();

        return count;
    }

    //로그인 성공 이벤트
    @Override
    public void updateLoginSuccess(String userId) {
        Optional<UserVo> option = changeUserPort.findByUserId(userId);
        if(option.isEmpty()) {
            return;
        }

        UserVo userVo = option.get();
        userVo.setLgnFailCnt(0);
        userVo.setLastLgnDtm(Instant.now());

        changeUserPort.flush();
    }
}
