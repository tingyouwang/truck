package com.luzhu.truck.entity.otherlendmoney;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "other_lend_money")
@Data
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

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Column(name = "rebill_source_other_lend_money_id")
    private Integer rebillSourceOtherLendMoneyId;

    @Column(name = "rebill_target_other_lend_money_id")
    private Integer rebillTargetOtherLendMoneyId;

    @Column(name = "disable", nullable = false)
    private int disable;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private long lastModifyTime;
    private String expenseYearMonth;
}
