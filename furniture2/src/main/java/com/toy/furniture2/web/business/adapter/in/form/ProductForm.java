package com.toy.furniture2.web.business.adapter.in.form;

import lombok.Data;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductForm {
    
    private String modelName;
    private String description;
    private Integer price;
    private String category;
    private String detailDescription;
    private String businessNumber;
    private List<DimensionForm> dimensions;

    private String analyzedColor;
    private MultipartFile productImage;
    private MultipartFile dimensionImage;



    @Data
    public static class DimensionForm {
        private String keyName;
        private String valueText;
    }

}