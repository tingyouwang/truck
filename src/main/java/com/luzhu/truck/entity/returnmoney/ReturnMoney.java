package com.luzhu.truck.entity.returnmoney;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "return_money")
@Data
public class ReturnMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "pay_date", nullable = false)
    private String payDate; // Using String as per preference

    @Column(name = "expense_year_month", length = 7, nullable = false)
    private String expenseYearMonth;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "disable", nullable = false)
    private int disable;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private Long lastModifyTime;
}
