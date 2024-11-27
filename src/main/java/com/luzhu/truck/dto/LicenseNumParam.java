package com.luzhu.truck.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LicenseNumParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
}
