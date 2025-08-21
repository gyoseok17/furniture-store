package com.toy.furniture2.web.business.application.port.out;

import java.util.List;

import com.toy.furniture2.web.business.adapter.in.form.ProductForm;
import com.toy.furniture2.web.common.domain.ProductDto;

public interface BusinessProductPort {

    String findBusinessNumberByUserId(String userId);
    List<ProductDto> findProductsByBusinessNumberAndFilters(String businessNumber, String keyword, List<String> categories);
    void registerProduct(ProductForm form);
    void deleteProductById(Integer id);
                         
}