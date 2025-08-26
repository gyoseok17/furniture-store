package com.toy.furniture2.web.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "business_user")
public class BusinessUserVo {
    @Id
    @Size(max = 50)
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Size(max = 100)
    @NotNull
    @Column(name = "business_name", nullable = false, length = 100)
    private String businessName;

    @ColumnDefault("'N'")
    @Column(name = "approved_yn")
    private String approvedYn;

    @Size(max = 30)
    @NotNull
    @Column(name = "business_number", nullable = false, length = 30)
    private String businessNumber;

    // Company 엔티티와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_number", insertable = false, updatable = false)
    private CompanyVo company;
}