package com.luzhu.truck.dto.car;

import com.luzhu.truck.dto.BaseParam;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LicenseNumPageParam extends BaseParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
}
