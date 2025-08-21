package com.toy.furniture2.web.common.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDto {
    private Integer id;
    private String modelName;
    private String description;
    private Integer price;
    private String imageUrl;
    private String category;
    private String businessNumber;

}
