package com.luzhu.truck.dto.insurancefeesetting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddInsuranceFeeSettingParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "保險公司不可為空")
    private String insuranceCom;
    @NotNull(message = "起保日不可為空")
    private LocalDate startDate;
    @NotNull(message = "截止保日不可為空")
    private LocalDate endDate;
    private LocalDate payUsDate;
    @NotNull(message = "保費不可為空")
    private double amount;
    @NotBlank(message = "保險種類不可為空")
    private String insuranceType;
    @NotBlank(message = "保單號碼不可為空")
    private String insuranceNum;
    @NotBlank(message = "保卡號碼不可為空")
    private String insuranceCardNum;
    private LocalDate quitDate;

}
