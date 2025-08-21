package com.toy.furniture2.web.user.adapter.in.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private String userId;
    private String pwd;
    private String userNm;
    private String email;
    private String zipcode;
    private String address;
    private String addressDetail;
}
