package com.toy.furniture2.web.furniture.shop.application.service;

import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.toy.furniture2.web.furniture.shop.application.port.in.SearchFurnitureShopUseCase;
import com.toy.furniture2.web.furniture.shop.application.port.out.SearchFurnitureShopPort;

@RequiredArgsConstructor
@Service
public class SearchFurnitureShopService implements SearchFurnitureShopUseCase {

    private final SearchFurnitureShopPort searchFurnitureShopPort;

    @Override
    public SearchProductVo findByProduct(int id) {
        return searchFurnitureShopPort.findByProduct(id);
    }

}
