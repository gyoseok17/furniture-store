package com.toy.furniture2.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import java.nio.file.Paths;

@Configuration
public class Config implements WebMvcConfigurer {
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.url-prefix}")
    private String uploadUrlPrefix;

    // @ResponseBody model을 json형태로 보내기위해 필요
    @Bean(name="jsonView")
    public MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

    // Spring Security 비밀번호 인코딩을위해 사용
    @Bean
    public PasswordEncoder PasswordEncoder () {
        return new BCryptPasswordEncoder();
    }

    // 외부 폴더를 정적 리소스로 서빙하는 표준 방법
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(System.getProperty("user.dir")).resolve("uploads").toString();
        registry
            .addResourceHandler("/uploads/**") // URL 경로
            .addResourceLocations("file:" + uploadPath + "/"); // 실제 폴더 경로
    }

    // RestTemplate 빈 등록 (외부 API 호출을 위해 사용. 파이썬API)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
