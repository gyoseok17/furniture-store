package com.toy.furniture2.web.member.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.toy.furniture2.web.member.adapter.in.form.BusinessForm;
import com.toy.furniture2.web.member.adapter.in.form.UserBusinessForm;
import com.toy.furniture2.web.member.adapter.in.form.UserForm;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class ScreenUserController {

    // ✅ 로그인 화면
    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("fragments/layout");

        // 아이디 저장 쿠키 확인
        String savedUserId = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> savedIdCookie = Arrays.stream(cookies)
                    .filter(cookie -> "saved_user_id".equals(cookie.getName()))
                    .findFirst();
            if (savedIdCookie.isPresent()) {
                savedUserId = savedIdCookie.get().getValue();
            }
        }
        mav.addObject("savedUserId", savedUserId);
        mav.addObject("page", "user/login");
        return mav;
    }

    // 유저 가입페이지
    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        ModelAndView mav = new ModelAndView("fragments/layout"); // layout.html 사용
        mav.addObject("page", "user/register"); // register.html을 Thymeleaf에서 로드
        return mav;
    }

    // 일반 회원 가입페이지
    @GetMapping("/userRegister")
    public ModelAndView showGeneralRegister() {
        ModelAndView mav = new ModelAndView("fragments/layout");
        mav.addObject("page", "user/userRegister");
        mav.addObject("user", UserForm.builder().build());
        return mav;
    }

    // 비즈니스 회원 가입페이지
    @GetMapping("/businessRegister")
    public ModelAndView showBusinessRegister() {
        ModelAndView mav = new ModelAndView("fragments/layout");
        mav.addObject("page", "user/businessRegister");

        mav.addObject("userBusinessForm", UserBusinessForm.builder()
                .userForm(UserForm.builder().build())
                .businessForm(BusinessForm.builder().build())
                .build());

        return mav;
    }

}
