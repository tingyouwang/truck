package com.luzhu.truck.entity.givebackmoney;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "expire_date", nullable = false)
    private String expireDate;

    @Column(name = "interest_amount", nullable = false)
    private BigDecimal interestAmount;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private long lastModifyTime;
    private String expenseYearMonth;
}
