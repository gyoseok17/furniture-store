package com.toy.furniture2.web.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Size(max = 30)
    @NotNull
    @Column(name = "business_number", nullable = false, length = 30)
    private String businessNumber;

    @ColumnDefault("'N'")
    @Column(name = "approved_yn")
    private String approvedYn;

}