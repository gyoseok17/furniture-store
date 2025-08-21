package com.toy.furniture2.web.furniture.shop.domain;

import lombok.Data;
import java.util.List;

@Data
public class PaymentRequestDto {
    private String userId;
    private String name;
    private String phone;
    private String postcode;
    private String address;
    private String detailAddress;
    private String message;
    private Integer totalAmount;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Integer productId;
        private Integer quantity;
    }
}