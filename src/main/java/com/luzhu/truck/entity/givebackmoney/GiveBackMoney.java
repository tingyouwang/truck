package com.luzhu.truck.entity.givebackmoney;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "give_back_money")
@Data
public class GiveBackMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "give_back_date", nullable = false)
    private String giveBackDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Column(name = "rebill_source_give_back_money_id")
    private Integer rebillSourceGiveBackMoneyId;

    @Column(name = "rebill_target_give_back_money_id")
    private Integer rebillTargetGiveBackMoneyId;

    @Column(name = "expire_date", nullable = false)
    private String expireDate;

    @Column(name = "interest_amount", nullable = false)
    private BigDecimal interestAmount;

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
