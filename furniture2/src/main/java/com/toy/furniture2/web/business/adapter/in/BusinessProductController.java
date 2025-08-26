package com.toy.furniture2.web.business.adapter.in;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.toy.furniture2.web.business.adapter.in.form.ProductForm;
import com.toy.furniture2.web.business.application.port.in.BusinessProductUseCase;
import com.toy.furniture2.web.common.domain.ProductDto;
import com.toy.furniture2.web.member.domain.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/business")
public class BusinessProductController {

    private final BusinessProductUseCase businessProductUseCase;

    // 상품 리스트 페이지
    @GetMapping("/productList")
    public String businessProductPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) List<String> category,
            Model model) {

        String userId = userDetails.getUsername();
        String businessNumber = businessProductUseCase.getBusinessNumber(userId);

        List<ProductDto> products = businessProductUseCase
            .getFilteredProducts(businessNumber, keyword, category);

        model.addAttribute("products", products);
        model.addAttribute("page", "business/productList");

        return "fragments/layout";
    }

    // 상품 등록 페이지
    @GetMapping("/newProduct")
    public String showProductForm(Model model) {
        model.addAttribute("page", "business/newProduct");
        return "fragments/layout";
    }

    // 상품 삭제
    @PostMapping("/productDelete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        businessProductUseCase.deleteProductById(id);
        return "redirect:/business/productList";
    }

    // ✅ 상품 등록 처리 (색상 분석 결과 포함)
    @PostMapping("/newProduct")
    public String registerProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @ModelAttribute ProductForm form,
                                @RequestParam(value = "colorMatchResult", required = false) String colorJson) {

        String businessNumber = businessProductUseCase.getBusinessNumber(userDetails.getUsername());
        form.setBusinessNumber(businessNumber);

        if (colorJson != null && !colorJson.isEmpty()) {
            form.setAnalyzedColor(colorJson);  // JSON 문자열 그대로 저장
        }

        businessProductUseCase.registerProduct(form);
        return "redirect:/business/productList";
    }

    // 상품 색상 분석 API
    @PostMapping("/ColorAnalyzer")
    public ResponseEntity<String> analyzeColor(@RequestParam("file") MultipartFile image) {
        try {
            String json = businessProductUseCase.sendToFlaskForAnalyze(image); // JSON 문자열 반환
            return ResponseEntity.ok(json); // 그대로 전송
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"색상 분석 실패: " + e.getMessage() + "\"}");
        }
    }

}
