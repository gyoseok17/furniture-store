package com.toy.furniture2.infrastructure.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.toy.furniture2.web.user.domain.CustomUserDetails;
import com.toy.furniture2.web.user.domain.SearchUserDto;
import com.toy.furniture2.web.user.application.port.in.ChangeUserUseCase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import jakarta.servlet.http.Cookie;

@RequiredArgsConstructor
public class RoleChoiceLoginFilter  extends UsernamePasswordAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final ChangeUserUseCase changeUserUseCase;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String userId = request.getParameter("userId");
        String password = request.getParameter("pwd");
        String role = request.getParameter("role");

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        if (userDetails instanceof CustomUserDetails customUser) {
            SearchUserDto userDto = customUser.getSearchUserDto();

            if (!userDto.getRole().equals(role) && !userDto.getRole().equals("ADMIN")) { //잘못된 역할로 로그인
                throw new CustomException("선택한 역할이 올바르지 않습니다.");
            }
            if(userDto.getRole().equals("BUSINESS") && userDto.getApprovedYn().equals("N")) {
                throw new CustomException("승인되지 않은 계정입니다. 관리자에게 문의하세요.");
            }

            if(userDto.getLockYn().equals("Y")) { //잠긴 계정
                throw new CustomException("잠긴 계정입니다. 관리자에게 문의하세요.");
            }

            LocalDateTime lastDtm = LocalDateTime.ofInstant(userDto.getLastLgnDtm(), ZoneId.systemDefault());
            if (lastDtm.isBefore(LocalDateTime.now().minusMonths(6))) { //마지막 로그인 일자 확인 - 휴면 여부
                changeUserUseCase.leavUserLogin(userId);
                throw new CustomException("휴면 계정입니다. 관리자에게 문의하세요.");
            }
        }

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(userId, password);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {


        String userId = request.getParameter("userId");

        if (failed instanceof BadCredentialsException) { //비밀번호 틀림
            int count = changeUserUseCase.updateLoginFailEvent(userId);

            if (count >= 5) { //5회 이상 틀림
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("<script>alert('비밀번호를 5회 연속 틀려 계정이 잠겼습니다. 관리자에게 문의하세요.'); location.href='/login';</script>");
                response.getWriter().flush();
                return;
            }
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("<script>alert('비밀번호를 틀렸습니다. 5번 연속 틀릴 시 계정이 잠깁니다. " + count + "/5 '); location.href='/login';</script>");
            response.getWriter().flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("<script>alert('" + failed.getMessage() + "'); location.href='/login';</script>");
            response.getWriter().flush();
        }
        
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        // 🔐 로그인 성공 후 인증 정보 확인
        System.out.println("✅ 로그인 성공한 사용자 ID: " + authResult.getName());
        System.out.println("✅ 권한: " + authResult.getAuthorities());
        changeUserUseCase.updateLoginSuccess(authResult.getName());

        // (1) SecurityContext 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        // (2) 세션에 SecurityContext 저장 → 이게 <sec:authorize> 작동에 핵심!
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        // (3) 아이디 저장 쿠키 처리
        String rememberId = request.getParameter("rememberId");
        String userId = authResult.getName();

        if (rememberId != null && rememberId.equals("on")) {
            Cookie cookie = new Cookie("saved_user_id", userId);
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("saved_user_id", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        response.sendRedirect("/furniture/main");
    }
}