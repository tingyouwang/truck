package com.luzhu.truck.dto.insurancefeesetting;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateInsuranceFeeSettingStatusParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "新保卡號碼不可為空")
    private String insuranceCardNum;
}
