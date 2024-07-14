package com.luzhu.truck.entity.licensetax;

import com.luzhu.truck.entity.BaseCompositePk;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "license_tax")
@IdClass(BaseCompositePk.class)
@Data
public class LicenseTax {
    @Id
    private String carLicenseNum;
    @Id
    private String expenseYearMonth;
    private BigDecimal amount;
    private String createTime;
}
