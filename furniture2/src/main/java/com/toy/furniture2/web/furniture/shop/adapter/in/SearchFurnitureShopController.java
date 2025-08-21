package com.toy.furniture2.web.furniture.shop.adapter.in;

import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;
import com.toy.furniture2.web.furniture.shop.application.port.in.SearchFurnitureShopUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/furniture/shop")
public class SearchFurnitureShopController {

    private final SearchFurnitureShopUseCase searchFurnitureShopUseCase;

    //상품 상세 페이지
    @GetMapping("/detail/{productId}")
    @ResponseBody
    public Map<String, Object> productDetail(@PathVariable("productId") int productId) {
        SearchProductVo byId = searchFurnitureShopUseCase.findByProduct(productId);
        return Map.of("detailProduct", byId);
    }
}
