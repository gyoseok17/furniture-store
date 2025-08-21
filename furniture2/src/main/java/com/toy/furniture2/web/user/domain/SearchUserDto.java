package com.toy.furniture2.web.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchUserDto {
    private String userId;
    private String pwd;
    private String userNm;
    private String tellNo;
    private String email;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String useYn;
    private String leavYn;
    private String lockYn;
    private Integer lgnFailCnt;
    private Instant lastLgnDtm;
    private Instant regDtm;
    private String role;
    private String approvedYn; // 비즈니스 유저 승인 여부


}
