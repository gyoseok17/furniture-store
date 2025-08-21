package com.toy.furniture2.web.admin.adapter.in;

import com.toy.furniture2.web.admin.application.port.in.LoadUserListUseCase;
import com.toy.furniture2.web.admin.application.port.in.ManageUserApprovalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LoadUserListUseCase loadUserListUseCase;
    private final ManageUserApprovalUseCase manageUserApprovalUseCase;

    @GetMapping("/user_list")
    public ModelAndView showUserList() {
        ModelAndView mav = new ModelAndView("fragments/layout");
        Map<String, List<?>> users = loadUserListUseCase.loadUsers();
        mav.addObject("GeneralUsers", users.get("GeneralUsers"));
        mav.addObject("BusinessUsers", users.get("BusinessUsers"));
        mav.addObject("page", "admin/user_list");
        return mav;
    }

    @PostMapping("/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable("userId") String userId) {
        manageUserApprovalUseCase.approveBusinessUser(userId);
        return ResponseEntity.ok("승인 완료");
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<String> rejectUser(@PathVariable("userId") String userId) {
        manageUserApprovalUseCase.rejectBusinessUser(userId);
        return ResponseEntity.ok("거절 완료");
    }
} 