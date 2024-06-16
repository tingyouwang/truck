package com.luzhu.truck.entity.insurancefeesetting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "insurance_fee_setting")
@Data
public class InsuranceFeeSetting {
    @Id
    @Column(name = "car_license_num")
    private String carLicenseNum;

    @Column(name = "insurance_com")
    private String insuranceCom;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    //入帳日
    @Column(name = "pay_us_date")
    private LocalDate payUsDate;

    @Column(name = "amount")
    private double amount;

    @Column(name = "insurance_type")
    private String insuranceType;

    @Column(name = "insurance_num")
    private String insuranceNum;

    @Column(name = "insurance_card_num")
    private String insuranceCardNum;

    @Column(name = "quit_date")
    private LocalDate quitDate;

    @Column(name = "create_time", updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDate createTime;

    @Column(name = "update_time", insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDate updateTime;

    @Column(name = "update_by")
    private String updateBy;

}
