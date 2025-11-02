package com.luzhu.truck.dto.loanfeesetting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateLoanFeeSettingStatusParam {
    @NotNull(message = "車貸設定ID不可為空")
    private Integer id;
    
    @Pattern(regexp = "^(enable|disable)$", message = "狀態只能是 enable 或 disable")
    private String status;
}

