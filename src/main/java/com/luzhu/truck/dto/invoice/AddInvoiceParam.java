package com.luzhu.truck.dto.invoice;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddInvoiceParam {
    @NotBlank(message = "type不可為空")
    //SALE:銷發, OFFSET:抵發, GAS:抵油
    @Pattern(regexp = "SALE|OFFSET|GAS", flags = Pattern.Flag.UNICODE_CASE)
    private String type;
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "處理日期不可為空")
    private String handleDate;
    @NotBlank(message = "發票日期不可為空")
    private String invoiceDate;
    @NotBlank(message = "發票號碼不可為空")
    private String invoiceNum;
    @NotNull(message = "銷貨金額")
    private BigDecimal invoiceAmount;
    @NotNull(message = "銷貨稅")
    private BigDecimal invoiceTax;
    @NotBlank(message = "車行")
    private String carAgency;
    private int carAgencyNum;
    private String note;
    private int disable;
    private String taxMonth;
}
