package com.toy.furniture2.web.furniture.main.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchDimensionVo {
    private String id;
    private String productId;
    private String keyName;
    private String valueText;
}
