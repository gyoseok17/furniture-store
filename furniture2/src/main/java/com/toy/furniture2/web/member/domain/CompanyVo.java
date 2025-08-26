package com.toy.furniture2.web.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class CompanyVo {
    
    @Id
    @Size(max = 30)
    @NotNull
    @Column(name = "business_number", nullable = false, length = 30)
    private String businessNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "business_name", nullable = false, length = 100)
    private String businessName;
}
