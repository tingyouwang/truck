package com.luzhu.truck.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCompositePk implements Serializable {
    private String carLicenseNum;
    private String expenseYearMonth;
}
