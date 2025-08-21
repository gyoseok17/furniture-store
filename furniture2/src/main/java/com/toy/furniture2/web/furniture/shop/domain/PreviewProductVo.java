package com.toy.furniture2.web.furniture.shop.domain;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviewProductVo {
    private int id;
    private String modelName;
    private int price;
    private String imageUrl;
    private String description;
    private String category;
    private Instant createAt;
}