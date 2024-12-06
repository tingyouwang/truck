package com.luzhu.truck.dto.insurancefeesetting;

import lombok.Data;

@Data
public class GetSingleInsuranceSettingParam {
    private String carLicenseNum;
    private String insuranceCardNum;
}
