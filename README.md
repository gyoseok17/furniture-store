## Furniture2 — JPA·JOOQ 하이브리드 가구 쇼핑몰 웹앱

### 개요
- 일반/사업자/관리자 역할 기반의 가구 쇼핑몰 웹 서비스입니다.
- JPA + JOOQ 하이브리드와 Hexagonal(Port-Adapter) 아키텍처로 생산성과 성능을 균형 있게 달성했습니다.

### 설명
- **아키텍처 설계**: Port-Adapter로 컨트롤러/유즈케이스/어댑터 분리, 테스트·교체 용이
- **데이터 접근**: JPA(단순 CRUD) + JOOQ(복잡 조인·동적 조건·집계) 병행
- **보안/인증**: Spring Security 커스텀 로그인 필터(역할 선택, 잠금/휴면 처리, 쿠키 저장)
- **화면/레이아웃**: Thymeleaf 레이아웃 구성, Devtools LiveReload
- **DB/조회**: JOOQ 코드 생성 테이블(`PRODUCTS`, `PRODUCT_DIMENSIONS`, `USER`, `BUSINESS_USER` 등) 기반 타입 안전 쿼리

### 핵심 기술 스택
- **Backend**: Spring Boot 3, Java 21
- **ORM/SQL**: Spring Data JPA, JOOQ 3.19
- **DB**: MariaDB
- **View**: Thymeleaf, Vanilla JS/CSS
- **Security**: Spring Security(커스텀 필터)
- **Build**: Gradle(Kotlin DSL), Devtools

### 아키텍처 요약
- **회원 흐름**: 가입(일반/사업자) → 관리자 승인(사업자) → 로그인(역할 선택) → 세션/권한 부여
- **쇼핑 흐름**: 카테고리별 목록(JOOQ) → 위시리스트 담기/조회 → 결제 요청 흐름
- **관리 흐름**: 회원 리스트 조회(JOOQ) → 사업자 승인/거절(상태 업데이트)

### 데이터 접근 요약
- **JPA**: `UserVo`, `BusinessUserVo` 엔티티 + `UserRepository`, `BusinessUserRepository`
- **JOOQ**: `DSLContext` 주입(`JooqConfig`) + 어댑터(`AdminPersistenceAdapter`, `SearchUserAdapter`, `ScreenFurnitureShopAdapter` 등)

### 빠른 실행 (Windows)
```cmd
cd furniture2
gradlew.bat bootRun
```
- 기본 포트: 8086 → http://localhost:8086

### 대표 엔드포인트
- 사용자: `GET /user/login`, `GET /user/register`, `GET /user/checkUserId?userId=...`
- 쇼핑: `GET /furniture/main`, `POST|GET|DELETE /furniture/shop/wishlist`
- 관리자: `GET /admin/user_list`, `POST /admin/approve/{userId}`, `POST /admin/reject/{userId}`

### 설정(요약)
```yaml
server.port: 8086
spring.datasource.url: jdbc:mariadb://localhost:3306/furniture
spring.datasource.username: root
spring.datasource.password: 3302
spring.jpa.hibernate.ddl-auto: update
```

### 폴더 구조(요약)
- `web/**/adapter/in`: 컨트롤러
- `web/**/adapter/out`: 영속 어댑터(JPA/JOOQ)
- `web/**/application/port`: 유즈케이스/포트 인터페이스
- `web/**/application/service`: 유즈케이스 구현
- `infrastructure/config`: 공통/보안/JOOQ 설정
