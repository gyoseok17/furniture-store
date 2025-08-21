package com.toy.furniture2.web.furniture.shop.adapter.in;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toy.furniture2.web.furniture.shop.application.port.in.PaymentUseCase;
import com.toy.furniture2.web.furniture.shop.domain.PaymentRequestDto;
import com.toy.furniture2.web.user.domain.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/furniture/shop/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentUseCase paymentUseCase;

    @GetMapping("")
    public String paymentPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (userDetails == null) {
            return "redirect:/user/login";
        }

        List<?> items = paymentUseCase.getOrderItems(userDetails.getUsername());
        model.addAttribute("items", items);
        model.addAttribute("page", "furniture/payment");
        
        return "fragments/layout";
    }

    @PostMapping("/process")
    @ResponseBody
    public String processPayment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PaymentRequestDto paymentRequestDto) {
        if (userDetails == null) {
            return "로그인이 필요합니다.";
        }
        try {
            paymentRequestDto.setUserId(userDetails.getUsername());
            paymentUseCase.processPayment(paymentRequestDto);
            return "success";
        } catch (Exception e) {
            log.error("결제 처리 실패", e);
            return "결제 처리 중 오류가 발생했습니다.";
        }
    }

}