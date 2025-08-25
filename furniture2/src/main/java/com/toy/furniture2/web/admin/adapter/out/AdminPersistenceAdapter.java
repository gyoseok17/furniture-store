package com.toy.furniture2.web.admin.adapter.out;

import com.toy.furniture2.web.admin.application.port.out.LoadAdminUserPort;
import com.toy.furniture2.web.admin.application.port.out.UpdateAdminUserPort;
import com.toy.furniture2.web.user.domain.SearchBusinessUserDto;
import com.toy.furniture2.web.user.domain.SearchUserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.User.USER;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.BusinessUser.BUSINESS_USER;


@Repository
@RequiredArgsConstructor
public class AdminPersistenceAdapter implements LoadAdminUserPort, UpdateAdminUserPort {

    private final DSLContext dsl;

    @Override
    public List<SearchUserDto> findGeneralUsers() {
        return dsl.selectFrom(USER)
                .where(USER.ROLE.eq("GENERAL"))
                .fetchInto(SearchUserDto.class);
    }

    @Override
    public List<SearchBusinessUserDto> findBusinessUsers() {
        return dsl.select(
                    USER.USER_ID,
                    USER.USER_NM,
                    BUSINESS_USER.BUSINESS_NAME,
                    BUSINESS_USER.BUSINESS_NUMBER,
                    BUSINESS_USER.APPROVED_YN
                )
                .from(USER)
                .join(BUSINESS_USER).on(USER.USER_ID.eq(BUSINESS_USER.USER_ID))
                .fetchInto(SearchBusinessUserDto.class);
    }

    @Override
    public void approveBusinessUser(String userId) {
        dsl.update(BUSINESS_USER)
                .set(BUSINESS_USER.APPROVED_YN, "Y")
                .where(BUSINESS_USER.USER_ID.eq(userId))
                .execute();
    }

    @Override
    public void rejectBusinessUser(String userId) {
        // 유저 테이블만 지우면 비즈니스 테이블에도 같이 삭제됨
        dsl.deleteFrom(USER)
                .where(USER.USER_ID.eq(userId))
                .execute();
    }
} 