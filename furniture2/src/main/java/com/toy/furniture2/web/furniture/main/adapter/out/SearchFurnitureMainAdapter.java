package com.toy.furniture2.web.furniture.main.adapter.out;

import com.toy.furniture2.web.furniture.main.application.port.out.SearchFurnitureMainPort;
import com.toy.furniture2.web.furniture.main.domain.SearchDimensionVo;
import com.toy.furniture2.web.furniture.main.domain.SearchProductVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.jooq.generated.Tables.PRODUCT_DIMENSIONS;
import static com.example.jooq.generated.tables.Products.PRODUCTS;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchFurnitureMainAdapter implements SearchFurnitureMainPort {

    private final DSLContext dsl;

    @Override
    public List<SearchProductVo> searchTop3Product() {
        List<SearchProductVo> top3Products = dsl
                .selectFrom(PRODUCTS)
                .limit(3)
                .fetchInto(SearchProductVo.class);

        for (SearchProductVo product : top3Products) {
            List<SearchDimensionVo> dimensions = dsl
                    .selectFrom(PRODUCT_DIMENSIONS)
                    .where(PRODUCT_DIMENSIONS.PRODUCT_ID.eq(Integer.parseInt(product.getId())))
                    .fetchInto(SearchDimensionVo.class);

            product.setDimensionsVoList(dimensions);
        }

        return top3Products;
    }

    @Override
    public List<String> getCategories(){
        return dsl
            .selectDistinct(PRODUCTS.CATEGORY)
            .from(PRODUCTS)
            .fetch(PRODUCTS.CATEGORY);
    }
}
