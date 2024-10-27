package com.luzhu.truck.entity.invoice;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
@Data
public class Invoice {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "car_license_num")
    private String carLicenseNum;

    @Column(name = "invoice_num", nullable = false, length = 100)
    private String invoiceNum;

    @Column(name = "invoice_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private LocalDate invoiceDate;

    @Column(name = "handle_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private LocalDate handleDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "amount_tax", nullable = false)
    private BigDecimal amountTax;

    @Column(name = "car_agency", nullable = false, length = 90)
    private String carAgency;

    @Column(name = "car_agency_num", nullable = false)
    private int carAgencyNum;

    @Column(name = "disable", nullable = false)
    private int disable;

    @Column(name = "note", length = 150)
    private String note;
    @Column(name = "taxMonth", nullable = false, length = 150)
    private String taxMonth;
    private String expenseYearMonth;
    private String type;
}
