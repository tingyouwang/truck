package com.luzhu.truck.entity.healthfee;

import lombok.Data;

import java.io.Serializable;

@Data
public class HealthFeeCompositePk implements Serializable {
    private String carLicenseNum;
    private String expenseYearMonth;
}
