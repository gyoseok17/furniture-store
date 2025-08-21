package com.toy.furniture2.web.furniture.shop.application.port.in;

import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

public interface SearchFurnitureShopUseCase {

    SearchProductVo findByProduct(int id);
    
}
