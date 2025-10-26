package com.luzhu.truck.entity.insurancecompany;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "insurance_company")
@Data
public class InsuranceCompany {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyName;
    private String shortName;
    private String contactor;
    private String phone;
    private String note;
    // 狀態 正常:enable 失效:disable
    private String status = "enable";
}
