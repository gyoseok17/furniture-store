package com.toy.furniture2.web.furniture.main.application.port.in;

import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

import java.util.List;

public interface SearchFurnitureMainUseCase {

    List<SearchProductVo> searchTop3Product();
    List<String> getCategories();
}
