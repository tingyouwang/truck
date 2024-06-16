package com.luzhu.truck.dto.insurancefeesetting;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteInsuranceSettingParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "保卡號碼不可為空")
    private String insuranceCardNum;
}
