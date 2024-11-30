package com.luzhu.truck.entity.insurancefeesetting;


import lombok.Data;

import java.time.LocalDate;

@Data
public class InsuranceFeeSettingDto {
    private String carLicenseNum;
    private String insuranceCardNum;

    private String insuranceCom;

    private String startDate;

    private String endDate;

    //入帳日
    private String payUsDate;

    private double amount;

    private String insuranceType;

    private String insuranceNum;

    private String quitDate;

    private Long createTime;

    private Long updateTime;

    private String updateBy;
    private String status;
}
