package com.luzhu.truck.entity.monthbillsnapshot;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "month_bill_snapshot")
@Data
public class MonthBillSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "car_license_num")
    private String carLicenseNum;
    
    @Column(name = "bill_year_month")
    private String billYearMonth;
    
    @Column(name = "last_month_owe_amount")
    private BigDecimal lastMonthOweAmount;
    
    @Column(name = "manage_fee")
    private BigDecimal manageFee;
    
    @Column(name = "union_fee")
    private BigDecimal unionFee;
    
    @Column(name = "labor_insurance_fee")
    private BigDecimal laborInsuranceFee;
    
    @Column(name = "health_fee")
    private BigDecimal healthFee;
    
    @Column(name = "insurance_fee")
    private BigDecimal insuranceFee;
    
    @Column(name = "license_tax_fee")
    private BigDecimal licenseTaxFee;
    
    @Column(name = "fuel_tax_fee")
    private BigDecimal fuelTaxFee;
    
    @Column(name = "loan_fee")
    private BigDecimal loanFee;
    
    @Column(name = "ready_fee")
    private BigDecimal readyFee;
    
    @Column(name = "people_help_fee")
    private BigDecimal peopleHelpFee;
    
    @Column(name = "invoice_sale_amount")
    private BigDecimal invoiceSaleAmount;
    
    @Column(name = "invoice_sale_amount_tax")
    private BigDecimal invoiceSaleAmountTax;
    
    @Column(name = "invoice_offset_amount")
    private BigDecimal invoiceOffsetAmount;
    
    @Column(name = "invoice_offset_amount_tax")
    private BigDecimal invoiceOffsetAmountTax;
    
    @Column(name = "invoice_gas_amount")
    private BigDecimal invoiceGasAmount;
    
    @Column(name = "invoice_gas_amount_tax")
    private BigDecimal invoiceGasAmountTax;
    
    @Column(name = "lend_money")
    private BigDecimal lendMoney;
    
    @Column(name = "lend_money_interest")
    private BigDecimal lendMoneyInterest;
    
    @Column(name = "give_back_money")
    private BigDecimal giveBackMoney;
    
    @Column(name = "give_back_interest")
    private BigDecimal giveBackInterest;
    
    @Column(name = "other_lend_money_amount")
    private BigDecimal otherLendMoneyAmount;
    
    @Column(name = "other_give_back_money_amount")
    private BigDecimal otherGiveBackMoneyAmount;
    
    @Column(name = "traffic_sum")
    private BigDecimal trafficSum;
    
    @Column(name = "pay_interest")
    private BigDecimal payInterest;
    
    @Column(name = "receive_offset")
    private BigDecimal receiveOffset;
    
    @Column(name = "return_money")
    private BigDecimal returnMoney;
    
    @Column(name = "total_sum")
    private BigDecimal totalSum;
    
    @Column(name = "create_time")
    private Long createTime;
    
    @Column(name = "update_time")
    private Long updateTime;
    
    @Column(name = "snapshot_type")
    private String snapshotType;
    
    @Column(name = "remark")
    private String remark;
}



