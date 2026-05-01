package com.luzhu.truck.dto.payinterest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePayInterestParam {
    @NotNull(message = "id不可為空")
    private Long id;
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "處理日期不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String payDate;
    @NotNull(message = "金額必填")
    private BigDecimal amount;
    private int disable;
    private String note;
}
