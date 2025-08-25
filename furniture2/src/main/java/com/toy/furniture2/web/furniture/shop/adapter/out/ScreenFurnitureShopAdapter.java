package com.toy.furniture2.web.furniture.shop.adapter.out;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.toy.furniture2.web.furniture.shop.application.port.out.ScreenFurnitureShopPort;
import com.toy.furniture2.web.furniture.shop.domain.PreviewProductVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.toy.furniture2.infrastructure.jooq.generated.tables.Products.PRODUCTS;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScreenFurnitureShopAdapter implements ScreenFurnitureShopPort {

    private final DSLContext dsl;

    @Override
    public List<PreviewProductVo> loadByCategory(String category, int offset, int limit) {
        return dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.CATEGORY.eq(category))
                .orderBy(PRODUCTS.CREATED_AT.desc()) // 최신순
                .limit(limit)
                .offset(offset)
                .fetchInto(PreviewProductVo.class);
    }

    // @Override
    // public SearchProductVo findById(int id) {
    //     SearchProductVo detailProduct = dsl
    //             .selectFrom(PRODUCTS)
    //             .where(PRODUCTS.ID.eq(id))
    //             .fetchOneInto(SearchProductVo.class);

    //     if (detailProduct != null) {
    //         List<SearchDimensionVo> dimensions = dsl
    //                 .selectFrom(PRODUCT_DIMENSIONS)
    //                 .where(
    //                         PRODUCT_DIMENSIONS.PRODUCT_ID.eq(Integer.parseInt(detailProduct.getId()))
    //                                 .and(PRODUCT_DIMENSIONS.VALUE_TEXT.notEqual("")))
    //                 .fetchInto(SearchDimensionVo.class);
    //         detailProduct.setDimensionsVoList(dimensions);
    //     }
    //     return detailProduct;
    // }
    
}
