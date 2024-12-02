package com.luzhu.truck.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LicenseAndExpenseYearMonthParam extends BaseParam{
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "帳單月份")
    private String expenseYearMonth;
}
