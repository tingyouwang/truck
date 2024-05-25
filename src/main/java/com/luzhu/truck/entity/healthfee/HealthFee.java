package com.luzhu.truck.entity.healthfee;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "health_fee")
@IdClass(HealthFeeCompositePk.class)
@Data
public class HealthFee {
    @Id
    private String carLicenseNum;
    @Id
    private String expenseYearMonth;
    private BigDecimal amount;
    private String createTime;
}
