package com.luzhu.truck.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateInvoiceParam {
    @NotNull
    private int id;
    @NotBlank(message = "處理日期不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String handleDate;
    @NotBlank(message = "發票日期不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String invoiceDate;
//    @NotBlank(message = "發票號碼不可為空")
    private String invoiceNum;
    @NotNull(message = "銷貨金額")
    private BigDecimal amount;
    @NotBlank(message = "車行")
    private String carAgency;
    private int carAgencyId;
    private String note;
    private int disable;
    private String taxMonth;
}
