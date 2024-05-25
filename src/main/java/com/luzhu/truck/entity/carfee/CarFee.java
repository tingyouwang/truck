package com.luzhu.truck.entity.carfee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "car_fee")
@Data
public class CarFee {
    @Id
    @Column(name = "car_license_num")
    private String carLicenseNum;

    @Column(name = "manage_fee")
    private Double manageFee;

    @Column(name = "sale_tax")
    private Double saleTax;

    @Column(name = "buy_tax")
    private Double buyTax;

    @Column(name = "gas_tax")
    private Double gasTax;

    @Column(name = "owe_tax")
    private Double oweTax;

    @Column(name = "receip_tax")
    private Double receipTax;

    @Column(name = "fuel_tax_spring")
    private Double fuelTaxSpring;

    @Column(name = "fuel_tax_summer")
    private Double fuelTaxSummer;

    @Column(name = "fuel_tax_autumn")
    private Double fuelTaxAutumn;

    @Column(name = "fuel_tax_winter")
    private Double fuelTaxWinter;

    @Column(name = "license_tax_first_half")
    private Double licenseTaxFirstHalf;

    @Column(name = "license_tax_second_half")
    private Double licenseTaxSecondHalf;

    @Column(name = "union_fee")
    private Double unionFee;

    @Column(name = "labor_fee")
    private Double laborFee;

    @Column(name = "healthy_fee")
    private Double healthyFee;

    @Column(name = "ready_fee")
    private Double readyFee;

    @Column(name = "people_help_fee")
    private Double peopleHelpFee;

    @Column(name = "create_time")
//    @Temporal(TemporalType.TIMESTAMP)
    private String createTime;

    @Column(name = "update_time")
//    @Temporal(TemporalType.TIMESTAMP)
    private String updateTime;

    @Column(name = "update_by")
    private String updateBy;
}
