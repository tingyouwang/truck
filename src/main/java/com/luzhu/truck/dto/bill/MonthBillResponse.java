package com.luzhu.truck.dto.bill;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthBillResponse {
    // 上月欠款
    private BigDecimal lastMonthOweAmount;

    // 管理費
    private BigDecimal manageFee;

    // 公會費
    private BigDecimal unionFee;

    // 車貸
    private BigDecimal loanFee;

    // 勞保
    private BigDecimal laborInsuranceFee;

    // 健保費
    private BigDecimal healthFee;

    // 保費
    private BigDecimal insuranceFee;

    // 牌照稅
    private BigDecimal licenseTaxFee;

    // 燃料稅
    private BigDecimal fuelTaxFee;
    // 銷發票額
    private BigDecimal invoiceSaleAmount;
    // 銷發票稅
    private BigDecimal invoiceSaleAmountTax;
    // 抵發票額
    private BigDecimal invoiceOffsetAmount;
    // 抵發票稅
    private BigDecimal invoiceOffsetAmountTax;
    // 抵油單額
    private BigDecimal invoiceGasAmount;
    // 抵油單稅
    private BigDecimal invoiceGasAmountTax;
    // 借款金額
    private BigDecimal lendMoney;
    // 借款利息
    private BigDecimal lendMoneyInterest;
    // 入款金額
    private BigDecimal giveBackMoney;
    // 入票利息
    private BigDecimal giveBackInterest;
    // 其他應收
    private BigDecimal otherLendMoneyAmount;
    // 其他抵收
    private BigDecimal otherGiveBackMoneyAmount;
    // 罰單
    private BigDecimal trafficSum;
    //代支利息
    private BigDecimal payInterest;
    //收據底收
    private BigDecimal receiveOffset;
    //入款退回
    private BigDecimal returnMoney;
}
