package com.toy.furniture2.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.toy.furniture2.infrastructure.config.security.filter.RoleChoiceLoginFilter;
import com.toy.furniture2.web.user.application.port.in.ChangeUserUseCase;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${user.role.admin}") String ADMIN;
    @Value("${user.role.business}") String BUSINESS;
    @Value("${user.role.general}") String GENERAL;
    String[] allPermitPath = {"/furniture/**", "/user/**", "/js/**", "/image/**", "/css/**"};
    String[] adminPsermitPath = {"/chart", "/admin/**", "/board/notice"};

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final ChangeUserUseCase changeUserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // ✅ 커스텀 로그인 필터 생성
        RoleChoiceLoginFilter customFilter = new RoleChoiceLoginFilter(userDetailsService, changeUserService);
        customFilter.setAuthenticationManager(authenticationManager());
        customFilter.setFilterProcessesUrl("/user/login"); // 로그인 요청 URL 지정
        customFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login", "POST"));

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(allPermitPath).permitAll()
                .requestMatchers(adminPsermitPath).hasRole(ADMIN)
                .anyRequest().authenticated()
            )
            .addFilterAt(customFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form
                .loginPage("/user/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/user/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    @Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

