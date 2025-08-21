package com.toy.furniture2.web.furniture.main.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchProductVo {
    private String id;
    private String modelName;
    private String description;
    private String price;
    private String imageUrl;
    private String category;
    private String detailUrl;
    private String dimensionImage;
    private Instant createAt;
    private List<SearchDimensionVo> dimensionsVoList;
}
