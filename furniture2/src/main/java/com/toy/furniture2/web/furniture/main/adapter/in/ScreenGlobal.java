package com.toy.furniture2.web.furniture.main.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.toy.furniture2.web.furniture.main.application.port.in.SearchFurnitureMainUseCase;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ScreenGlobal {

    private final SearchFurnitureMainUseCase searchFurnitureMainUseCase;

    @ModelAttribute("categories") // 모든 컨트롤러에서 categories를 사용할 수 있도록 설정
    public List<String> categories() {
        return searchFurnitureMainUseCase.getCategories();
    }
}
