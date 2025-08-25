package com.toy.furniture2.web.business.adapter.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.toy.furniture2.web.business.adapter.in.form.ProductForm;
import com.toy.furniture2.web.business.application.port.out.BusinessProductPort;
import com.toy.furniture2.web.common.domain.ProductDto;

import lombok.RequiredArgsConstructor;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.Products.PRODUCTS;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.BusinessUser.BUSINESS_USER;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.ProductDimensions.PRODUCT_DIMENSIONS;

@Repository
@RequiredArgsConstructor
public class BusinessProductAdapter implements BusinessProductPort {

    private final DSLContext dsl;

    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.url-prefix}")
    private String uploadUrlPrefix;

    @Override
    public String findBusinessNumberByUserId(String userId) {
        return dsl.select(BUSINESS_USER.BUSINESS_NUMBER)
                  .from(BUSINESS_USER)
                  .where(BUSINESS_USER.USER_ID.eq(userId))
                  .fetchOneInto(String.class);
    }

    @Override
    public List<ProductDto> findProductsByBusinessNumberAndFilters(String businessNumber, String keyword, List<String> categories) {
        var condition = PRODUCTS.BUSINESS_NUMBER.eq(businessNumber);

        if (keyword != null && !keyword.isBlank()) {
            condition = condition.and(PRODUCTS.MODEL_NAME.containsIgnoreCase(keyword));
        }
        if (categories != null && !categories.isEmpty()) {
            condition = condition.and(PRODUCTS.CATEGORY.in(categories));
        }

        return dsl.selectFrom(PRODUCTS)
                  .where(condition)
                  .fetch()
                  .stream()
                  .map(record -> ProductDto.builder()
                      .id(record.getId())
                      .modelName(record.getModelName())
                      .description(record.getDescription())
                      .price(record.getPrice())
                      .imageUrl(record.getImageUrl())
                      .category(record.getCategory())
                      .businessNumber(record.getBusinessNumber())
                      .build())
                  .toList();
    }

    @Override
    @Transactional
    public void registerProduct(ProductForm form) {
        try {
            // 0. 파일 저장
            MultipartFile productImage = form.getProductImage();
            String savedImageUrl = null;
            if (productImage != null && !productImage.isEmpty()) {
                savedImageUrl = saveImage(productImage);
            }

            // 0-1. 치수 이미지 저장
            MultipartFile dimensionImage = form.getDimensionImage();
            String savedDimensionImageUrl = null;
            if (dimensionImage != null && !dimensionImage.isEmpty()) {
                savedDimensionImageUrl = saveImage(dimensionImage);
            }

            // 1. 상품 등록
            Integer productId = dsl.insertInto(PRODUCTS)
                .set(PRODUCTS.MODEL_NAME, form.getModelName())
                .set(PRODUCTS.DESCRIPTION, form.getDescription())
                .set(PRODUCTS.PRICE, form.getPrice())
                .set(PRODUCTS.IMAGE_URL, savedImageUrl)
                .set(PRODUCTS.CATEGORY, form.getCategory())
                .set(PRODUCTS.ANALYZED_COLOR, form.getAnalyzedColor())
                .set(PRODUCTS.DETAIL_DESCRIPTION, form.getDetailDescription())
                .set(PRODUCTS.DIMENSION_IMAGE, savedDimensionImageUrl)
                .set(PRODUCTS.BUSINESS_NUMBER, form.getBusinessNumber())
                .returning(PRODUCTS.ID)
                .fetchOne()
                .getId();

            if (productId == null) {
                throw new RuntimeException("상품 등록 실패: productId가 null입니다.");
            }

            // 2. 치수들 등록
            if (form.getDimensions() != null && !form.getDimensions().isEmpty()) {
                // 중복 키 체크
                Set<String> keyNames = new HashSet<>();
                
                for (ProductForm.DimensionForm dim : form.getDimensions()) {
                    if (dim == null || dim.getKeyName() == null || dim.getKeyName().trim().isEmpty()) {
                        continue; // 유효하지 않은 치수 정보는 건너뜀
                    }
                    
                    // 중복 키 체크
                    if (!keyNames.add(dim.getKeyName().trim())) {
                        throw new RuntimeException("중복된 치수 키가 있습니다: " + dim.getKeyName());
                    }
                    
                    // 치수 값 유효성 검사
                    if (dim.getValueText() == null || dim.getValueText().trim().isEmpty()) {
                        throw new RuntimeException("치수 값이 비어있습니다: " + dim.getKeyName());
                    }
                    
                    dsl.insertInto(PRODUCT_DIMENSIONS)
                        .set(PRODUCT_DIMENSIONS.PRODUCT_ID, productId)
                        .set(PRODUCT_DIMENSIONS.KEY_NAME, dim.getKeyName().trim())
                        .set(PRODUCT_DIMENSIONS.VALUE_TEXT, dim.getValueText().trim())
                        .execute();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("상품 등록 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 상품 등록 시 이미지 저장
    public String saveImage(MultipartFile file) {
        try {
            // 한글 파일명 깨짐 방지
            String originalFilename = file.getOriginalFilename();
            String safeFilename = UUID.randomUUID() + "_" + 
                                (originalFilename != null ? originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_") : "image");

            // 프로젝트 폴더 내에 uploads 폴더 생성
            Path uploadDir = Paths.get(System.getProperty("user.dir")).resolve("uploads");
            
            // 디렉토리가 없으면 생성
            Files.createDirectories(uploadDir);
            
            // 파일 저장
            Path path = uploadDir.resolve(safeFilename);
            file.transferTo(path.toFile());

            // 웹에서 접근 가능한 URL 경로 반환
            return "/uploads/" + safeFilename;

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        try {
            // 1. 상품 정보 조회
            var product = dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(id))
                .fetchOne();

            if (product != null) {
                // 2. 이미지 파일 삭제
                String imageUrl = product.getImageUrl();
                String dimensionImageUrl = product.getDimensionImage();
                
                // 상품 이미지 삭제
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    deleteImageFile(imageUrl);
                }
                
                // 치수 이미지 삭제
                if (dimensionImageUrl != null && !dimensionImageUrl.isEmpty()) {
                    deleteImageFile(dimensionImageUrl);
                }

                // 3. 치수 정보 삭제
                dsl.deleteFrom(PRODUCT_DIMENSIONS)
                   .where(PRODUCT_DIMENSIONS.PRODUCT_ID.eq(id))
                   .execute();

                // 4. 상품 삭제
                dsl.deleteFrom(PRODUCTS)
                   .where(PRODUCTS.ID.eq(id))
                   .execute();
            }
        } catch (Exception e) {
            throw new RuntimeException("상품 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 이미지 파일 삭제 메서드
    private void deleteImageFile(String imageUrl) {
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // URL에서 파일 경로 추출
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                Path uploadDir = Paths.get(System.getProperty("user.dir")).resolve("uploads");
                Path filePath = uploadDir.resolve(fileName);
                
                // 파일이 존재하면 삭제
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    System.out.println("파일 삭제 성공: " + filePath);
                } else {
                    System.out.println("파일이 존재하지 않음: " + filePath);
                }
            }
        } catch (IOException e) {
            // 파일 삭제 실패 시 로그만 남기고 계속 진행
            System.err.println("이미지 파일 삭제 실패: " + e.getMessage());
        }
    }

}