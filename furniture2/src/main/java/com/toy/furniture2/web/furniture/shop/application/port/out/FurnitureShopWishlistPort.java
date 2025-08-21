package com.toy.furniture2.web.furniture.shop.application.port.out;

import java.util.List;
import java.util.Map;

public interface FurnitureShopWishlistPort {
    
    List<Map<String, Object>> getWishlist(String userId);
    void removeWishlist(String userId, int productId);
    void updateWishlistQuantity(String userId, int productId, String action);

    boolean exists(String userId, int productId);
    void updateQuantity(String userId, int productId, int quantity);
    void insert(String userId, int productId, int quantity);
    
}
