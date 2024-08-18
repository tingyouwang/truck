package com.luzhu.truck.dto.othergivebackmoney;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddOtherGiveBackMoneyParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "入款日期不可為空")
    private String giveBackDate;
    @NotNull(message = "借款金額")
    private BigDecimal amount;
    private String note;
}
