package com.luzhu.truck.dto.loanfeesetting;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetSingleLoanFeeSettingParam {
    @NotNull(message = "車貸設定ID不可為空")
    private Integer id;
}

