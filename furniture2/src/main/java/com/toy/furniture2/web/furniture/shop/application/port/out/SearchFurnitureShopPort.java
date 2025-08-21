package com.toy.furniture2.web.furniture.shop.application.port.out;

import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

public interface SearchFurnitureShopPort {
    
    SearchProductVo findByProduct(int id);
    
}
