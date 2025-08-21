package com.toy.furniture2.web.furniture.main.application.port.out;

import java.util.List;

import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

public interface SearchFurnitureMainPort {

    List<SearchProductVo> searchTop3Product();
    List<String> getCategories();
}
