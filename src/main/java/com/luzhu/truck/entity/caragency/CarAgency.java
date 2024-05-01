package com.luzhu.truck.entity.caragency;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "car_agency")
@Data
public class CarAgency {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String agencyName;
    private String address;
    private String owner;
    private String taxId;
    private String phone1;
    private String phone2;
    private String mobile;
    private String fax;
}
