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
    // 抵發稅額
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
    //收據抵收
    private BigDecimal receiveOffset;
    //入款退回
    private BigDecimal returnMoney;
    //本月欠款
    private BigDecimal totalSum;
    public void calculateTotalSum() {
        this.totalSum = BigDecimal.ZERO
                .add(lastMonthOweAmount != null ? lastMonthOweAmount : BigDecimal.ZERO)
                .add(manageFee != null ? manageFee : BigDecimal.ZERO)
                .add(unionFee != null ? unionFee : BigDecimal.ZERO)
                .add(loanFee != null ? loanFee : BigDecimal.ZERO)
                .add(laborInsuranceFee != null ? laborInsuranceFee : BigDecimal.ZERO)
                .add(healthFee != null ? healthFee : BigDecimal.ZERO)
                .add(insuranceFee != null ? insuranceFee : BigDecimal.ZERO)
                .add(licenseTaxFee != null ? licenseTaxFee : BigDecimal.ZERO)
                .add(fuelTaxFee != null ? fuelTaxFee : BigDecimal.ZERO)
                .add(invoiceGasAmount != null ? invoiceGasAmount : BigDecimal.ZERO)
                .subtract(invoiceGasAmountTax != null ? invoiceGasAmountTax : BigDecimal.ZERO)
                .add(invoiceSaleAmount != null ? invoiceSaleAmount : BigDecimal.ZERO)
                .add(invoiceSaleAmountTax != null ? invoiceSaleAmountTax : BigDecimal.ZERO)
                .add(invoiceOffsetAmount != null ? invoiceOffsetAmount : BigDecimal.ZERO)
                .subtract(invoiceOffsetAmountTax != null ? invoiceOffsetAmountTax : BigDecimal.ZERO)
                .add(lendMoney != null ? lendMoney : BigDecimal.ZERO)
                .add(lendMoneyInterest != null ? lendMoneyInterest : BigDecimal.ZERO)
                .subtract(giveBackMoney != null ? giveBackMoney : BigDecimal.ZERO)
                .add(giveBackInterest != null ? giveBackInterest : BigDecimal.ZERO)
                .add(otherLendMoneyAmount != null ? otherLendMoneyAmount : BigDecimal.ZERO)
                .subtract(otherGiveBackMoneyAmount != null ? otherGiveBackMoneyAmount : BigDecimal.ZERO)
                .add(trafficSum != null ? trafficSum : BigDecimal.ZERO)
                .add(payInterest != null ? payInterest : BigDecimal.ZERO)
                .subtract(receiveOffset != null ? receiveOffset : BigDecimal.ZERO)
                .add(returnMoney != null ? returnMoney : BigDecimal.ZERO);
    }
}
