package com.toy.furniture2.web.user.adapter.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;

import com.toy.furniture2.web.user.domain.SearchUserDto;
import com.toy.furniture2.web.user.application.port.out.SearchUserPort;

import static com.toy.furniture2.infrastructure.jooq.generated.tables.User.USER;
import static com.toy.furniture2.infrastructure.jooq.generated.tables.BusinessUser.BUSINESS_USER;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchUserAdapter implements SearchUserPort {

    private final DSLContext dsl;

    //UserDetailsService
    @Override
    public SearchUserDto findByUserId(String userId) {
        return dsl.select(
                    USER.USER_ID,
                    USER.PWD,
                    USER.USE_YN,
                    USER.LEAV_YN,
                    USER.LOCK_YN,
                    USER.LGN_FAIL_CNT,
                    USER.LAST_LGN_DTM,
                    USER.ROLE,
                    BUSINESS_USER.APPROVED_YN
                )
                .from(USER)
                .leftJoin(BUSINESS_USER).on(USER.USER_ID.eq(BUSINESS_USER.USER_ID))
                .where(USER.USER_ID.eq(userId))
                .fetchOneInto(SearchUserDto.class);
    }

    //중복 검사
    @Override
    public int countByUserId(String userId) {
        Integer count = dsl.selectCount()
                .from(USER)
                .where(USER.USER_ID.eq(userId))
                .fetchOne(Record1::value1);

        if(count == null) {
            count = 0;
        }

        return count;
    }
}
