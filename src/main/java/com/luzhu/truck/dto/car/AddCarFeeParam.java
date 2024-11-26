package com.luzhu.truck.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCarFeeParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;       // 車牌號碼
    @NotNull
    private double manageFee;           // 管理費
    @NotNull
    private double saleTax;             // 銷項稅率
    @NotNull
    private double buyTax;              // 進項稅率 for 車輛總帳的抵發票額
    @NotNull
    private double gasTax;              // 油單稅率
    @NotNull
    private double oweTax;              // 欠款利率
    @NotNull
    private double receipTax;           // 收據稅率
    @NotNull
    private double fuelTaxSpring;       // 春燃料稅
    @NotNull
    private double fuelTaxSummer;       // 夏燃料稅
    @NotNull
    private double fuelTaxAutumn;       // 秋燃料稅
    @NotNull
    private double fuelTaxWinter;       // 冬燃料稅
    @NotNull
    private double licenseTaxFirstHalf; // 牌照稅上半年
    @NotNull
    private double licenseTaxSecondHalf;// 牌照稅下半年
    @NotNull
    private double unionFee;            // 公會費
    @NotNull
    private double laborFee;            // 勞保
    @NotNull
    private double healthyFee;          // 健保
    @NotNull
    private double readyFee;            // 準備金
    @NotNull
    private double peopleHelpFee;       // 互助金
//    private String createTime;   // 創建時間
//    private String updateTime;   // 更新時間
//    private String updateBy;            // 更新者
}
