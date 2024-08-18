package com.luzhu.truck.dto.trafficticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddTrafficTicketParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "處理日期不可為空")
    private String handleDate;
    @NotBlank(message = "罰單日期不可為空")
    private String ticketDate;
    @NotBlank(message = "到案日期不可為空")
    private String goPoliceDate;
    @NotBlank(message = "罰單案號不可為空")
    private String ticketNum;
    @NotNull(message = "罰單金額")
    private BigDecimal amount;
    @NotBlank(message = "代繳日期不可為空")
    private String payDate;
    private String note;
}
