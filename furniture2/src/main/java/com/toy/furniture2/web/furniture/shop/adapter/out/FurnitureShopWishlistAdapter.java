package com.toy.furniture2.web.furniture.shop.adapter.out;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.toy.furniture2.infrastructure.jooq.generated.tables.records.UserWishlistRecord;
import com.toy.furniture2.web.furniture.shop.application.port.out.FurnitureShopWishlistPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.toy.furniture2.infrastructure.jooq.generated.tables.Products.PRODUCTS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.UserWishlist.USER_WISHLIST;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FurnitureShopWishlistAdapter implements FurnitureShopWishlistPort{

    private final org.jooq.DSLContext dsl;

    @Override
    public List<Map<String, Object>> getWishlist(String userId) {
        return dsl.select(
                    PRODUCTS.ID.as("productId"),
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
    public void removeWishlist(String userId, int productId) {
        dsl.deleteFrom(USER_WISHLIST)
                .where(USER_WISHLIST.USER_ID.eq(userId)
                        .and(USER_WISHLIST.PRODUCT_ID.eq(productId)))
                .execute();
    }

    @Override // 위시리스트 수량 버튼 이벤트
    public void updateWishlistQuantity(String userId, int productId, String action) {
        UserWishlistRecord wishlistItem = dsl.selectFrom(USER_WISHLIST)
                .where(USER_WISHLIST.USER_ID.eq(userId)
                        .and(USER_WISHLIST.PRODUCT_ID.eq(productId)))
                .fetchOne();

        if (wishlistItem == null) return;

        int currentQty = wishlistItem.getQuantity();
        if ("increase".equals(action)) {
            wishlistItem.setQuantity(currentQty + 1);
        } else if ("decrease".equals(action) && currentQty > 1) {
            wishlistItem.setQuantity(currentQty - 1);
        } else {
            return; // 수량 1 이하로는 안 내려가게
        }

        dsl.update(USER_WISHLIST)
                .set(USER_WISHLIST.QUANTITY, wishlistItem.getQuantity())
                .where(USER_WISHLIST.USER_ID.eq(userId)
                        .and(USER_WISHLIST.PRODUCT_ID.eq(productId)))
                .execute();
    }

    @Override
    public boolean exists(String userId, int productId) {
        return dsl.fetchExists(
                dsl.selectFrom(USER_WISHLIST)
                        .where(USER_WISHLIST.USER_ID.eq(userId))
                        .and(USER_WISHLIST.PRODUCT_ID.eq(productId))
        );
    }

    @Override // 위시리스트에 상품이 없으면 추가, 있으면 수량만 업데이트
    public void updateQuantity(String userId, int productId, int quantity) {
        dsl.update(USER_WISHLIST)
                .set(USER_WISHLIST.QUANTITY, USER_WISHLIST.QUANTITY.plus(quantity))
                .where(USER_WISHLIST.USER_ID.eq(userId))
                .and(USER_WISHLIST.PRODUCT_ID.eq(productId))
                .execute();
    }

    @Override
    public void insert(String userId, int productId, int quantity) {
        dsl.insertInto(USER_WISHLIST)
                .set(USER_WISHLIST.USER_ID, userId)
                .set(USER_WISHLIST.PRODUCT_ID, productId)
                .set(USER_WISHLIST.QUANTITY, quantity)
                .execute();
    }

}
