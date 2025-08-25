package com.toy.furniture2.web.furniture.shop.adapter.out;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.toy.furniture2.web.furniture.shop.application.port.out.PaymentPort;
import com.toy.furniture2.web.furniture.shop.domain.PaymentRequestDto;

import lombok.RequiredArgsConstructor;

import static com.toy.furniture2.infrastructure.jooq.generated.tables.Products.PRODUCTS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.UserWishlist.USER_WISHLIST;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.Orders.ORDERS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.OrderItems.ORDER_ITEMS;


@Repository
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentPort {

    private final org.jooq.DSLContext dsl;

    @Override
    public List<Map<String, Object>> getOrderItems(String userId) {
        return dsl.select(
                    PRODUCTS.ID,
                    PRODUCTS.MODEL_NAME,
                    PRODUCTS.PRICE,
                    PRODUCTS.IMAGE_URL,
                    USER_WISHLIST.QUANTITY
                )
                .from(USER_WISHLIST)
                .join(PRODUCTS).on(USER_WISHLIST.PRODUCT_ID.eq(PRODUCTS.ID))
                .where(USER_WISHLIST.USER_ID.eq(userId))
                .fetchMaps();
    }

    @Override
    public void processPayment(PaymentRequestDto paymentRequestDto) {
        // 1. orders 테이블에 저장
        var orderRecord = dsl.insertInto(ORDERS)
            .set(ORDERS.USER_ID, paymentRequestDto.getUserId())
            .set(ORDERS.NAME, paymentRequestDto.getName())
            .set(ORDERS.PHONE, paymentRequestDto.getPhone())
            .set(ORDERS.POSTCODE, paymentRequestDto.getPostcode())
            .set(ORDERS.ADDRESS, paymentRequestDto.getAddress())
            .set(ORDERS.DETAIL_ADDRESS, paymentRequestDto.getDetailAddress())
            .set(ORDERS.MESSAGE, paymentRequestDto.getMessage())
            .set(ORDERS.TOTAL_AMOUNT, paymentRequestDto.getTotalAmount())
            .returning(ORDERS.ID)
            .fetchOne();

        Long orderId = orderRecord.getId();

        // 2. order_items 테이블에 상품별로 저장
        for (PaymentRequestDto.OrderItemDto item : paymentRequestDto.getItems()) {
            dsl.insertInto(ORDER_ITEMS)
                .set(ORDER_ITEMS.ORDER_ID, orderId)
                .set(ORDER_ITEMS.PRODUCT_ID, item.getProductId())
                .set(ORDER_ITEMS.QUANTITY, item.getQuantity())
                .execute();
        }

        // 3. 결제 후 위시리스트(혹은 장바구니) 비우기 (선택)
        dsl.deleteFrom(USER_WISHLIST)
            .where(USER_WISHLIST.USER_ID.eq(paymentRequestDto.getUserId()))
            .execute();
    }

}