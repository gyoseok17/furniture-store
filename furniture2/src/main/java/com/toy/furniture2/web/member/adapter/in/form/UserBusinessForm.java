package com.toy.furniture2.web.member.adapter.in.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBusinessForm {
    private UserForm userForm;
    private BusinessForm businessForm;
}
