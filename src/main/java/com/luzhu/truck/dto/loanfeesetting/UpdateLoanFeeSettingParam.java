package com.luzhu.truck.dto.loanfeesetting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateLoanFeeSettingParam {
    @NotNull(message = "車貸設定ID不可為空")
    private Integer id;
    
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    
    @NotBlank(message = "需填入貸款公司")
    private String loanCompany;
    
    @NotNull
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String startDate;
    
    @NotNull
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String endDate;
    
    @NotNull
    private BigDecimal totalAmount;
    
    @NotNull
    private BigDecimal monthPayAmount;
    
    private String note;
    
    @Pattern(regexp = "^(enable|disable)$", message = "狀態只能是 enable 或 disable")
    private String status;
}

