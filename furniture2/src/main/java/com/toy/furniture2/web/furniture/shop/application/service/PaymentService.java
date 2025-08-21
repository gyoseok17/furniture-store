package com.toy.furniture2.web.furniture.shop.application.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toy.furniture2.web.furniture.shop.application.port.in.PaymentUseCase;
import com.toy.furniture2.web.furniture.shop.application.port.out.PaymentPort;
import com.toy.furniture2.web.furniture.shop.domain.PaymentRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {
    private final PaymentPort paymentPort;

    @Override
    public List<Map<String, Object>> getOrderItems(String userId) {
        return paymentPort.getOrderItems(userId);
    }

    @Override
    @Transactional
    public void processPayment(PaymentRequestDto paymentRequestDto) {
        paymentPort.processPayment(paymentRequestDto);
    }

} 