package com.luzhu.truck.dto.insurancefeesetting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateInsuranceFeeSettingParam {
//    @NotBlank(message = "原保卡號碼不可為空")
//    private String originalInsuranceCardNum;
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "新保卡號碼不可為空")
    private String insuranceCardNum;

    @NotBlank(message = "保險公司不可為空")
    private String insuranceCom;
    @NotNull(message = "起保日不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String startDate;
    @NotNull(message = "截止保日不可為空")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String endDate;
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String payUsDate;
    @NotNull(message = "保費不可為空")
    private double amount;
    @NotBlank(message = "保險種類不可為空")
    private String insuranceType;
    @NotBlank(message = "保單號碼不可為空")
    private String insuranceNum;
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String quitDate;
}
