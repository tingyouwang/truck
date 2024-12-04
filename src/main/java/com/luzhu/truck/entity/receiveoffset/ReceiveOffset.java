package com.luzhu.truck.entity.receiveoffset;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receive_offset")
@Data
public class ReceiveOffset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "pay_date", nullable = false)
    private String payDate;

    @Column(name = "expense_year_month", length = 7, nullable = false)
    private String expenseYearMonth;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "receipt_amount", nullable = false)
    private BigDecimal receiptAmount;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private Long lastModifyTime;
}
