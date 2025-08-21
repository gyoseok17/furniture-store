package com.toy.furniture2.web.furniture.shop.adapter.in;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toy.furniture2.web.furniture.shop.application.port.in.FurnitureShopWishlistUseCase;
import com.toy.furniture2.web.user.domain.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/furniture/shop")
public class FurnitureShopWishlistController {

    private final FurnitureShopWishlistUseCase furnitureShopWishlistUseCase;

    // 위시리스트 담기
    @PostMapping("/wishlist")
    public ResponseEntity<?> addToWishlist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody Map<String, Object> body) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }

        Object productIdObj = body.get("productId");
        Object quantityObj = body.get("quantity");

        if (productIdObj == null || quantityObj == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "productId와 quantity는 필수입니다."));
        }

        int productId = Integer.parseInt(productIdObj.toString());
        int quantity = Integer.parseInt(quantityObj.toString());

        furnitureShopWishlistUseCase.addWishlist(userDetails.getUsername(), productId, quantity);
        return ResponseEntity.ok(Map.of("message", "장바구니에 담겼습니다."));
    }
    
    //장바구니 불러오기
    @GetMapping("/wishlist") //나중에 dto만들기?
    public ResponseEntity<?> getWishlist(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다"));
        }

        List<Map<String, Object>> wishlist = furnitureShopWishlistUseCase.getWishlist(userDetails.getUsername());
        return ResponseEntity.ok(wishlist);
    }

    //장바구니 목록 삭제
    @DeleteMapping("/wishlist/{productId}")
    @ResponseBody
    public ResponseEntity<Void> removeWishlist(@PathVariable("productId") int productId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        furnitureShopWishlistUseCase.removeWishlist(userDetails.getUsername(), productId);
        return ResponseEntity.ok().build();
    }

    //장바구니 수량 변경
    @PutMapping("/wishlist/{productId}")
    public ResponseEntity<Void> updateWishlistQuantity(@PathVariable("productId") int productId,
                                            @RequestBody Map<String, String> body,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String action = body.get("action");
        if (action == null) return ResponseEntity.badRequest().build();

        furnitureShopWishlistUseCase.updateWishlistQuantity(userDetails.getUsername(), productId, action);
        return ResponseEntity.ok().build();
    }

}
