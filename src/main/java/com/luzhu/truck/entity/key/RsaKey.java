package com.luzhu.truck.entity.key;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rsa_key")
public class RsaKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "public", nullable = false, length = 1000)
    private String publicKey;

    @Column(name = "private", nullable = false, length = 1000)
    private String privateKey;

    @Column(name = "module", nullable = false, length = 1000)
    private String module;
}
