package com.toy.furniture2.web.furniture.shop.adapter.out;

import com.toy.furniture2.web.furniture.main.domain.SearchDimensionVo;
import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jooq.DSLContext;

import com.toy.furniture2.web.furniture.shop.application.port.out.SearchFurnitureShopPort;
import org.springframework.stereotype.Repository;

import static com.toy.furniture2.infrastructure.jooq.generated.tables.ProductDimensions.PRODUCT_DIMENSIONS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.Products.PRODUCTS;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchFurnitureShopAdapter implements SearchFurnitureShopPort {

    private final DSLContext dsl;

    @Override
    public SearchProductVo findByProduct(int id) {
        SearchProductVo detailProduct = dsl
                .selectFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .fetchOneInto(SearchProductVo.class);


        if (detailProduct != null) {
            List<SearchDimensionVo> dimensions = dsl
                    .selectFrom(PRODUCT_DIMENSIONS)
                    .where(
                            PRODUCT_DIMENSIONS.PRODUCT_ID.eq(Integer.parseInt(detailProduct.getId()))
                                    .and(PRODUCT_DIMENSIONS.VALUE_TEXT.notEqual("")))
                    .fetchInto(SearchDimensionVo.class);
            detailProduct.setDimensionsVoList(dimensions);
        }
        return detailProduct;
    }

}