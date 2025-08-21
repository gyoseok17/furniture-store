package com.toy.furniture2.web.business.application.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.toy.furniture2.web.business.adapter.in.form.ProductForm;
import com.toy.furniture2.web.business.application.port.in.BusinessProductUseCase;
import com.toy.furniture2.web.business.application.port.out.BusinessProductPort;
import com.toy.furniture2.web.common.domain.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessProductService implements BusinessProductUseCase {

    private final BusinessProductPort businessProductPort;

    @Override
    public String getBusinessNumber(String userId) {
        return businessProductPort.findBusinessNumberByUserId(userId);
    }

    @Override
    public List<ProductDto> getFilteredProducts(String businessNumber, String keyword, List<String> categories) {
        return businessProductPort.findProductsByBusinessNumberAndFilters(businessNumber, keyword, categories);
    }

    @Override
    public void registerProduct(ProductForm form) {
        businessProductPort.registerProduct(form);
    }
    
    @Override
    public void deleteProductById(Integer id) {
        businessProductPort.deleteProductById(id);
    }

    private final RestTemplate restTemplate;
    @Value("${flask.api.url:http://localhost:5000/analyze-color}")
    private String flaskUrl;
    public String sendToFlaskForAnalyze(MultipartFile image) throws Exception {
        // 임시 파일로 저장
        File tempFile = File.createTempFile("upload-", image.getOriginalFilename());
        image.transferTo(tempFile);

        // 멀티파트 요청 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(tempFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, entity, String.class);

        return response.getBody();
    }

}