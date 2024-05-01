package com.luzhu.truck.entity.loancompany;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "loan_company")
@Data
public class LoanCompany {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyName;
    private String shortName;
    private String contactor;
    private String phone;
    private String note;

}
