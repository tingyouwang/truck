package com.luzhu.truck.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateInvoiceParam {
    @NotNull
    private int id;
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
