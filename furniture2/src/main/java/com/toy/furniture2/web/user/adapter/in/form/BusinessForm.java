package com.toy.furniture2.web.user.adapter.in.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessForm {
    private String businessName;
    private String businessNumber;
}
