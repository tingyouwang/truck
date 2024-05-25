package com.luzhu.truck.entity.laborInsurance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "labor_insurance")
@IdClass(LaborInsuranceCompositePk.class)
@Data
public class LaborInsurance {
    @Id
    private String carLicenseNum;
    @Id
    private String expenseYearMonth;
    private BigDecimal amount;
    private String createTime;
}
