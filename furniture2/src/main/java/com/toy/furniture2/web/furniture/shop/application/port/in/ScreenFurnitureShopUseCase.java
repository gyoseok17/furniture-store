package com.toy.furniture2.web.furniture.shop.application.port.in;

import java.util.List;

import com.toy.furniture2.web.furniture.shop.domain.PreviewProductVo;

public interface ScreenFurnitureShopUseCase {

    List<PreviewProductVo> loadMoreCategory(String category, int offset, int limit);
    
}
