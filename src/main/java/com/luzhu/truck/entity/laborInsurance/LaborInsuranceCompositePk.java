package com.luzhu.truck.entity.laborInsurance;

import lombok.Data;

import java.io.Serializable;

@Data
public class LaborInsuranceCompositePk implements Serializable {
    private String carLicenseNum;
    private String expenseYearMonth;
}
