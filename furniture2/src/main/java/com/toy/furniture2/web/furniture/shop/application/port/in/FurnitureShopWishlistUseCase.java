package com.toy.furniture2.web.furniture.shop.application.port.in;

import java.util.List;
import java.util.Map;

public interface FurnitureShopWishlistUseCase {
    
    List<Map<String, Object>> getWishlist(String userId);
    void removeWishlist(String userId, int productId);
    void updateWishlistQuantity(String userId, int productId, String action);
    void addWishlist(String userId, int productId, int quantity);
    
}
