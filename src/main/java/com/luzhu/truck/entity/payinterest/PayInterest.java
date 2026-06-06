package com.luzhu.truck.entity.payinterest;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "pay_interest")
@Data
public class PayInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "pay_date", nullable = false)
    private String payDate;

    @Column(name = "expense_year_month", length = 7, nullable = false)
    private String expenseYearMonth;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Column(name = "rebill_source_pay_interest_id")
    private Integer rebillSourcePayInterestId;

    @Column(name = "rebill_target_pay_interest_id")
    private Integer rebillTargetPayInterestId;

    @Column(name = "disable", nullable = false)
    private int disable;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private Long lastModifyTime;
}
