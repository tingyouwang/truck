package com.luzhu.truck.entity.owner;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "owner")
@Data
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "id_num", nullable = false, length = 100)
    private String idNum;

    @Column(name = "sex", length = 20)
    private String sex;

    @Column(name = "birthday", length = 20)
    private String birthday;

    @Column(name = "phone1", length = 20)
    private String phone1;

    @Column(name = "phone2", length = 20)
    private String phone2;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "mail_address", length = 300)
    private String mailAddress;
}
