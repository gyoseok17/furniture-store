package com.toy.furniture2.web.furniture.shop.application.port.out;

import java.util.List;

import com.toy.furniture2.web.furniture.shop.domain.PreviewProductVo;

public interface ScreenFurnitureShopPort {

    List<PreviewProductVo> loadByCategory(String category, int offset, int limit);
    
}