package com.toy.furniture2.web.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toy.furniture2.web.user.adapter.in.form.BusinessForm;
import com.toy.furniture2.web.user.adapter.in.form.UserBusinessForm;
import com.toy.furniture2.web.user.adapter.in.form.UserForm;
import com.toy.furniture2.web.user.domain.BusinessUserVo;
import com.toy.furniture2.web.user.domain.UserVo;
import com.toy.furniture2.web.user.application.port.in.ChangeUserUseCase;
import com.toy.furniture2.web.user.application.port.out.ChangeUserPort;



import java.time.Instant;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class ChangeUserService implements ChangeUserUseCase {

    private final ChangeUserPort changeUserPort;
    private final PasswordEncoder passwordEncoder;

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
