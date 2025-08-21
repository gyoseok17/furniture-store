package com.toy.furniture2.web.admin.application.service;

import com.toy.furniture2.web.admin.application.port.in.LoadUserListUseCase;
import com.toy.furniture2.web.admin.application.port.in.ManageUserApprovalUseCase;
import com.toy.furniture2.web.admin.application.port.out.LoadAdminUserPort;
import com.toy.furniture2.web.admin.application.port.out.UpdateAdminUserPort;
import com.toy.furniture2.web.user.domain.SearchBusinessUserDto;
import com.toy.furniture2.web.user.domain.SearchUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService implements LoadUserListUseCase, ManageUserApprovalUseCase {

    private final LoadAdminUserPort loadAdminUserPort;
    private final UpdateAdminUserPort updateAdminUserPort;

    //회원 리스트 가져오기
    @Override
    public Map<String, List<?>> loadUsers() {
        List<SearchUserDto> generalUsers = loadAdminUserPort.findGeneralUsers();
        List<SearchBusinessUserDto> businessUsers = loadAdminUserPort.findBusinessUsers();

        Map<String, List<?>> users = new HashMap<>();
        users.put("GeneralUsers", generalUsers);
        users.put("BusinessUsers", businessUsers);

        return users;
    }

    @Override
    @Transactional
    public void approveBusinessUser(String userId) {
        updateAdminUserPort.approveBusinessUser(userId);
    }

    @Override
    @Transactional
    public void rejectBusinessUser(String userId) {
        // 거절 시 회원 데이터를 바로 삭제
        updateAdminUserPort.rejectBusinessUser(userId);
    }
} 