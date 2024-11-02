package com.luzhu.truck.entity.healthfee;

import com.luzhu.truck.entity.BaseCompositePk;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "health_fee")
@IdClass(BaseCompositePk.class)
@Data
public class HealthFee {
    @Id
    private String carLicenseNum;
    @Id
    private String expenseYearMonth;
    private BigDecimal amount;
    private Long createTime;
}
