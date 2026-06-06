package com.luzhu.truck.entity.trafficticket;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "traffic_ticket")
@Data
public class TrafficTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "car_license_num", length = 50)
    private String carLicenseNum;

    @Column(name = "handle_date", nullable = false)
    private String handleDate;

    @Column(name = "ticket_date", nullable = false)
    private String ticketDate;

    @Column(name = "go_police_date", nullable = false)
    private String goPoliceDate;

    @Column(name = "pay_date", nullable = false)
    private String payDate;

    @Column(name = "ticket_num", length = 50)
    private String ticketNum;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Column(name = "rebill_source_traffic_ticket_id")
    private Integer rebillSourceTrafficTicketId;

    @Column(name = "rebill_target_traffic_ticket_id")
    private Integer rebillTargetTrafficTicketId;

    @Column(name = "disable", nullable = false)
    private int disable;

    @Column(name = "note", length = 150)
    private String note;

    @Column(name = "create_time", nullable = false)
    private long createTime;

    @Column(name = "last_modify_time", nullable = false)
    private long lastModifyTime;
    private String expenseYearMonth;
}
