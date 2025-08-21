package com.toy.furniture2.web.furniture.shop.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.context.SecurityContextHolder;

import com.toy.furniture2.web.furniture.shop.application.port.in.ScreenFurnitureShopUseCase;
import com.toy.furniture2.web.furniture.shop.domain.PreviewProductVo;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/furniture")
public class ScreenFurnitureShopController {

    private final ScreenFurnitureShopUseCase screenFurnitureShopUseCase;

    // 페이지 띄우기(카테고리별 상품 목록 조회)
    @GetMapping("/shop")
    public ModelAndView productShopByCategory(@RequestParam("category") String category) {
        int pageSize = 18;
        List<PreviewProductVo> products = screenFurnitureShopUseCase.loadMoreCategory(category, 0, pageSize);

        ModelAndView mav = new ModelAndView("fragments/layout");
        mav.addObject("page", "furniture/shop");
        mav.addObject("products", products); // ✅ 리스트만 전달
        mav.addObject("category", category); // ✅ JS에서 사용
        mav.addObject("isLoggedIn", SecurityContextHolder.getContext().getAuthentication() != null && 
            SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        return mav;
    }

    // 스크롤시 더 불러오기
    @GetMapping("/shop/load-more")
    @ResponseBody
    public List<PreviewProductVo> loadMore(
                                            @RequestParam("category") String category,
                                            @RequestParam("page") int page) {
        int pageSize = 18;
        int offset = (page - 1) * pageSize;
        return screenFurnitureShopUseCase.loadMoreCategory(category, offset, pageSize);
    }

}
