package com.toy.furniture2.web.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserVo {
    @Id
    @Size(max = 50)
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Size(max = 100)
    @NotNull
    @Column(name = "pwd", nullable = false, length = 100)
    private String pwd;

    @Size(max = 50)
    @Column(name = "user_nm", length = 50)
    private String userNm;

    @Size(max = 20)
    @Column(name = "tell_no", length = 20)
    private String tellNo;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 10)
    @Column(name = "zipcode", length = 10)
    private String zipcode;

    @Size(max = 200)
    @Column(name = "address", length = 200)
    private String address;

    @Size(max = 200)
    @Column(name = "address_detail", length = 200)
    private String addressDetail;

    @Size(max = 255)
    @ColumnDefault("'Y'")
    @Column(name = "use_yn")
    private String useYn;

    @Size(max = 255)
    @ColumnDefault("'N'")
    @Column(name = "leav_yn")
    private String leavYn;

    @Size(max = 255)
    @ColumnDefault("'N'")
    @Column(name = "lock_yn")
    private String lockYn;

    @ColumnDefault("0")
    @Column(name = "lgn_fail_cnt")
    private Integer lgnFailCnt;

    @Column(name = "last_lgn_dtm")
    private Instant lastLgnDtm;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_dtm")
    private Instant regDtm;

    @Size(max = 20)
    @Column(name = "role", length = 20)
    private String role;

}