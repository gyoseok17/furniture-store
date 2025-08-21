package com.toy.furniture2.web.furniture.main.adapter.in;


import com.toy.furniture2.web.furniture.main.application.port.in.SearchFurnitureMainUseCase;
import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/furniture/main")
public class SearchFurnitureMainController {

    private final SearchFurnitureMainUseCase searchFurnitureMainUseCase;

    @GetMapping
    public ModelAndView searchFurnitureMain(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("fragments/layout");

        List<SearchProductVo> top3ProductVoList = searchFurnitureMainUseCase.searchTop3Product();

        mav.addObject("isLoggedIn", SecurityContextHolder.getContext().getAuthentication() != null && 
            SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        mav.addObject("page", "furniture/main");
        mav.addObject("products", top3ProductVoList);

        return mav;
    }

}
