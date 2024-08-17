package com.luzhu.truck.entity.lendmoney;

import com.luzhu.truck.entity.BaseCompositePk;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "lend_money")
@Data
public class LendMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "lend_date", nullable = false)
    private LocalDate lendDate;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "expire_date", nullable = false)
    private LocalDate expireDate;

    @Column(name = "interest_amount", nullable = false)
    private double interestAmount;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private LocalDate createTime;

    @Column(name = "last_modify_time", nullable = false)
    private LocalDate lastModifyTime;
}
