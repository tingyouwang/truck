package com.luzhu.truck.entity.loanfeesetting;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "loan_fee_setting")
@Data
public class LoanFeeSetting {
    @Id
    @Column(name = "car_license_num")
    private String carLicenseNum;

    @Column(name = "loan_company")
    private String loanCompany;

    @Column(name = "start_date")
//    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Column(name = "end_date")
//    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "month_pay_amount")
    private double monthPayAmount;

}
