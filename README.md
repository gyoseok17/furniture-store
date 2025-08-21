## 가구 쇼핑몰 웹 — Java Spring Boot

### 개요
- 일반/사업자/관리자 역할 기반의 가구 쇼핑몰 웹 서비스입니다.
- 일반회원은 장바구니에 담아 구매할 수 있고, 판매자는 상품을 등록 및 관리, 관리자는 회원을 관리합니다.
- JPA + JOOQ 하이브리드와 Hexagonal(Port-Adapter) 아키텍처

### 설명
- **아키텍처 설계**: Port-Adapter로 컨트롤러/유즈케이스/어댑터 분리, 테스트·교체 용이
- **데이터 접근**: JPA(단순 CRUD) + JOOQ(복잡 조인·동적 조건·집계) 병행
- **보안/인증**: Spring Security 커스텀 로그인 필터(역할 선택, 잠금/휴면 처리, 쿠키 저장)
- **화면/레이아웃**: Thymeleaf 레이아웃 구성, Devtools LiveReload
- **DB/조회**: JOOQ 코드 생성 테이블 기반 타입 안전 쿼리

### 핵심 기술 스택
- **Backend**: Spring Boot 3, Java 21
- **ORM/SQL**: Spring Data JPA, JOOQ 3.19
- **DB**: MariaDB
- **View**: Thymeleaf, Vanilla JS/CSS
- **Security**: Spring Security(커스텀 필터)

### 아키텍처 요약
- **회원 흐름**: 가입(일반/사업자) → 관리자 승인(사업자) → 로그인(역할 선택) → 세션/권한 부여
- **쇼핑 흐름**: 카테고리별 목록(JOOQ) → 위시리스트 담기/조회 → 결제 요청 흐름
- **관리 흐름**: 회원 리스트 조회(JOOQ) → 사업자 승인/거절(상태 업데이트)
