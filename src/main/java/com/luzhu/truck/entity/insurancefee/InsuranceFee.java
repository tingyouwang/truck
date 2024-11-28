package com.luzhu.truck.entity.insurancefee;

import com.luzhu.truck.entity.BaseCompositePk;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insurance_fee")
@IdClass(BaseCompositePk.class)
@Data
public class InsuranceFee {
    @Id
    @Column(name = "car_license_num")
    private String carLicenseNum;

    @Id
    @Column(name = "expense_year_month")
    private String expenseYearMonth;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "create_time")
    private long createTime;
    @Column(name = "insurance_card_num")
    private String insuranceCardNum;
    @Column(name = "status")
    private String status;

}
