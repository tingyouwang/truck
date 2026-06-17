package com.luzhu.truck.dto.trafficticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddTrafficTicketParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "處理日期不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String handleDate;
    @NotBlank(message = "罰單日期不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String ticketDate;
    @NotBlank(message = "到案日期不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String goPoliceDate;
    @NotBlank(message = "罰單案號不可為空")
    private String ticketNum;
    @NotNull(message = "罰單金額")
    private BigDecimal amount;
    private int disable;
    private String payDate;
    private String note;
}
