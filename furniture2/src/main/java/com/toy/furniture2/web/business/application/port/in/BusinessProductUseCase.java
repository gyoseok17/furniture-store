package com.toy.furniture2.web.business.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.toy.furniture2.web.business.adapter.in.form.ProductForm;
import com.toy.furniture2.web.common.domain.ProductDto;

public interface BusinessProductUseCase {
    
    String getBusinessNumber(String userId);
    List<ProductDto> getFilteredProducts(String businessNumber, String keyword, List<String> categories);
    void registerProduct(ProductForm form);
    void deleteProductById(Integer id);
    String sendToFlaskForAnalyze(MultipartFile image) throws Exception;
}