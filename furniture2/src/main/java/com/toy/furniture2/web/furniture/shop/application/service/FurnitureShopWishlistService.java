package com.toy.furniture2.web.furniture.shop.application.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.toy.furniture2.web.furniture.shop.application.port.in.FurnitureShopWishlistUseCase;
import com.toy.furniture2.web.furniture.shop.application.port.out.FurnitureShopWishlistPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FurnitureShopWishlistService implements FurnitureShopWishlistUseCase{
    
    private final FurnitureShopWishlistPort furnitureShopWishlistPort;

    @Override
    public void addWishlist(String userId, int productId, int quantity) {
        if (furnitureShopWishlistPort.exists(userId, productId)) {
            furnitureShopWishlistPort.updateQuantity(userId, productId, quantity);
        } else {
            furnitureShopWishlistPort.insert(userId, productId, quantity);
        }
    }

    @Override
    public List<Map<String, Object>> getWishlist(String userId) {
        return furnitureShopWishlistPort.getWishlist(userId);
    }

    @Override
    public void removeWishlist(String userId, int productId) {
        furnitureShopWishlistPort.removeWishlist(userId, productId);
    }

    @Override
    public void updateWishlistQuantity(String userId, int productId, String action) {
        furnitureShopWishlistPort.updateWishlistQuantity(userId, productId, action);
    }

}
