package com.luzhu.truck.entity.loanfeesetting;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_fee_setting")
@Data
public class LoanFeeSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "car_license_num", nullable = false)
    private String carLicenseNum;

    @Column(name = "loan_company")
    private String loanCompany;

    @Column(name = "start_date")
//    @Temporal(TemporalType.DATE)
    private String startDate;
    @Column(name = "start_datetime")
//    @Temporal(TemporalType.DATE)
    private LocalDateTime startDatetime;

    @Column(name = "end_date")
//    @Temporal(TemporalType.DATE)
    private String endDate;
    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "month_pay_amount")
    private double monthPayAmount;
    private String note;

    // 狀態 正常:enable 失效:disable
    @Column(name = "status", nullable = false)
    private String status = "enable";

}
