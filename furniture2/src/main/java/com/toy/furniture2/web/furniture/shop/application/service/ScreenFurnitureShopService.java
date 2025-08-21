package com.toy.furniture2.web.furniture.shop.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.toy.furniture2.web.furniture.shop.application.port.in.ScreenFurnitureShopUseCase;
import com.toy.furniture2.web.furniture.shop.application.port.out.ScreenFurnitureShopPort;
import com.toy.furniture2.web.furniture.shop.domain.PreviewProductVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScreenFurnitureShopService implements ScreenFurnitureShopUseCase {

    private final ScreenFurnitureShopPort screenFurnitureShopPort;

    @Override
    public List<PreviewProductVo> loadMoreCategory(String category, int offset, int limit) {
        return screenFurnitureShopPort.loadByCategory(category, offset, limit);
    }

    // @Override
    // public SearchProductVo findById(int id) {
    //     return screenFurnitureShopPort.findById(id);
    // }

    // @Override
    // public List<Map<String, Object>> getWishlistList(String userId) {
    //     return screenFurnitureShopPort.getWishlistList(userId);
    // }

    // @Override
    // public void removeItem(String userId, int productId) {
    //     screenFurnitureShopPort.deleteByUserIdAndProductId(userId, productId);
    // }

    // @Override
    // public void updateItemQuantity(String userId, int productId, String action) {
    //     screenFurnitureShopPort.updateQuantity(userId, productId, action);
    // }
    
}
