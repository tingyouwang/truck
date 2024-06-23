package com.luzhu.truck.dto.loanfeesetting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddLoanFeeSettingParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "需填入貸款公司")
    private String loanCompany;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private BigDecimal totalAmount;
    @NotNull
    private BigDecimal monthPayAmount;

}
