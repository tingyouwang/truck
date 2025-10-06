package com.luzhu.truck.dto.car;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddCarFeeParam {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;       // 車牌號碼
    @NotNull
    private Double manageFee;           // 管理費
    @NotNull
    @DecimalMax("1")
    @DecimalMin("0.001")
    private Double saleTax;             // 銷項稅率
    @NotNull
    @DecimalMax("1")
    @DecimalMin("0.001")
    private Double buyTax;              // 進項稅率 for 車輛總帳的抵發票額
    @NotNull
    @DecimalMax("1")
    @DecimalMin("0.001")
    private Double gasTax;              // 油單稅率
    @NotNull
    @DecimalMax("1")
    @DecimalMin("0")
    private Double oweTax;              // 欠款利率
//    @NotNull
//    @DecimalMax("1")
//    @DecimalMin("0.001")
    private Double giveBackTax = 0.0;         // 入款利率
    @NotNull
    @DecimalMax("1")
    @DecimalMin("0")
    private Double receipTax;           // 收據稅率
    @NotNull
    private Double fuelTaxSpring;       // 春燃料稅
    @NotNull
    private Double fuelTaxSummer;       // 夏燃料稅
    @NotNull
    private Double fuelTaxAutumn;       // 秋燃料稅
    @NotNull
    private Double fuelTaxWinter;       // 冬燃料稅
    @NotNull
    private Double licenseTaxFirstHalf; // 牌照稅上半年
    @NotNull
    private Double licenseTaxSecondHalf;// 牌照稅下半年
    @NotNull
    private Double unionFee;            // 公會費
    @NotNull
    private Double laborFee;            // 勞保
    @NotNull
    private Double healthyFee;          // 健保
    @NotNull
    private Double readyFee;            // 準備金
    @NotNull
    private Double peopleHelpFee;       // 互助金
//    private String createTime;   // 創建時間
//    private String updateTime;   // 更新時間
//    private String updateBy;            // 更新者
}
