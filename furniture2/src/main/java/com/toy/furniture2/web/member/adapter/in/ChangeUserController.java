package com.toy.furniture2.web.member.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toy.furniture2.web.member.adapter.in.form.UserBusinessForm;
import com.toy.furniture2.web.member.adapter.in.form.UserForm;
import com.toy.furniture2.web.member.application.port.in.ChangeUserUseCase;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class ChangeUserController {

    private final ChangeUserUseCase changeUserUseCase;
    //일반 회원 가입
    @PostMapping("/userRegister")
    public ModelAndView insertGeneralRegister(@ModelAttribute UserForm userForm, RedirectAttributes redirectAttributes) {
        changeUserUseCase.insert(userForm);
        redirectAttributes.addFlashAttribute("message", "회원 가입 신청이 완료되었습니다.");
        return new ModelAndView("redirect:/user/login"); // 로그인 페이지로 이동
    }

    //사업자 회원 가입
    @PostMapping("/businessRegister")
    public ModelAndView insertBusinessRegister(@ModelAttribute UserBusinessForm userBusinessForm, RedirectAttributes redirectAttributes) {
        changeUserUseCase.insert(userBusinessForm);
        redirectAttributes.addFlashAttribute("message", "회원 가입 신청이 완료되었습니다. 관리자의 승인 후에 로그인 가능합니다.");
        return new ModelAndView("redirect:/user/login"); // 로그인 페이지로 이동
    }

}
