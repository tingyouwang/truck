package com.luzhu.truck.dto.bill;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateCurrentMonthBillReq {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
}
