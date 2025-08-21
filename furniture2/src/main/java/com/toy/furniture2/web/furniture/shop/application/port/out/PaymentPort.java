package com.toy.furniture2.web.furniture.shop.application.port.out;

import java.util.List;
import java.util.Map;

import com.toy.furniture2.web.furniture.shop.domain.PaymentRequestDto;

public interface PaymentPort {

    List<Map<String, Object>> getOrderItems(String userId);
    void processPayment(PaymentRequestDto paymentRequestDto);

} 