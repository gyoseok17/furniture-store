package com.toy.furniture2.web.furniture.main.application.service;

import com.toy.furniture2.web.furniture.main.application.port.in.SearchFurnitureMainUseCase;
import com.toy.furniture2.web.furniture.main.application.port.out.SearchFurnitureMainPort;
import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchFurnitureMainService implements SearchFurnitureMainUseCase {

    private final SearchFurnitureMainPort searchFurnitureMainPort;

    @Override
    public List<SearchProductVo> searchTop3Product() {
        return searchFurnitureMainPort.searchTop3Product();
    }

    @Override
    public List<String> getCategories() {
        return searchFurnitureMainPort.getCategories();
    }

}
