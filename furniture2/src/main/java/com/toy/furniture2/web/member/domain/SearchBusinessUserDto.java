package com.toy.furniture2.web.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchBusinessUserDto {

    private String userId;
    private String userNm;
    private String businessName;
    private String businessNumber;
    private String approvedYn;
    
}
