package com.luzhu.truck.entity.otherlendmoney;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "other_lend_money")
public class OtherLendMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "lend_date", nullable = false)
    private String lendDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private long lastModifyTime;
}
