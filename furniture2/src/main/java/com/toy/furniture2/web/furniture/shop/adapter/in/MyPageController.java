package com.toy.furniture2.web.furniture.shop.adapter.in;

import com.toy.furniture2.web.user.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.toy.furniture2.infrastructure.jooq.generated.tables.Products.PRODUCTS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.Orders.ORDERS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.OrderItems.ORDER_ITEMS;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@RequestMapping("/furniture/myPage")
@Controller
@RequiredArgsConstructor
public class MyPageController {

    //임시
    private final com.toy.furniture2.web.furniture.shop.application.port.out.FurnitureShopWishlistPort wishlistPort;
    private final org.jooq.DSLContext dsl;

    @GetMapping("")
    public String myPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (userDetails == null) {
            return "redirect:/user/login";
        }
        String userId = userDetails.getUsername();

        // 위시리스트 조회
        List<Map<String, Object>> wishlist = wishlistPort.getWishlist(userId);

        // 주문 내역 조회 (orders 테이블)
        List<Map<String, Object>> orders = dsl.select(
                ORDERS.ID,
                ORDERS.NAME,
                ORDERS.PHONE,
                ORDERS.ADDRESS,
                ORDERS.POSTCODE,
                ORDERS.DETAIL_ADDRESS,
                ORDERS.TOTAL_AMOUNT,
                ORDERS.CREATED_AT,
                ORDERS.MESSAGE
        )
                .from(ORDERS)
                .where(ORDERS.USER_ID.eq(userId))
                .orderBy(ORDERS.CREATED_AT.desc())
                .fetchMaps();

        List<Map<String, Object>> orderHistory = orders.stream()
                .map(order -> {
                    Long orderId = ((Number) order.get("id")).longValue();
                    List<Map<String, Object>> items = dsl.select(
                                    ORDER_ITEMS.QUANTITY,
                                    PRODUCTS.MODEL_NAME,
                                    PRODUCTS.IMAGE_URL,
                                    PRODUCTS.PRICE,
                                    PRODUCTS.ID.as("product_id")
                            )
                            .from(ORDER_ITEMS)
                            .join(PRODUCTS).on(ORDER_ITEMS.PRODUCT_ID.eq(PRODUCTS.ID))
                            .where(ORDER_ITEMS.ORDER_ID.eq(orderId))
                            .fetchMaps();
                    order.put("items", items);
                    return order;
                })
                .collect(Collectors.toList());


        // 전체 구매 상품 집계
        @SuppressWarnings("unchecked") //경고 무시
        Map<Long, Map<String, Object>> aggregatedItems = orderHistory.stream()
                .flatMap(order -> ((List<Map<String, Object>>) order.get("items")).stream())
                .collect(Collectors.toMap(
                        item -> ((Number) item.get("product_id")).longValue(),
                        item -> {
                            Map<String, Object> newItem = new LinkedHashMap<>(item);
                            newItem.put("total_quantity", item.get("quantity"));
                            return newItem;
                        },
                        (existingItem, newItem) -> {
                            int newQuantity = (int) existingItem.get("quantity") + (int) newItem.get("quantity");
                            existingItem.put("total_quantity", newQuantity);
                            return existingItem;
                        },
                        LinkedHashMap::new
                ));

        List<Map<String, Object>> purchasedProducts = aggregatedItems.values().stream().collect(Collectors.toList());


        model.addAttribute("wishlist", wishlist);
        model.addAttribute("orderHistory", orderHistory);
        model.addAttribute("purchasedProducts", purchasedProducts);
        model.addAttribute("page", "furniture/myPage");
        return "fragments/layout";
    }

    
}