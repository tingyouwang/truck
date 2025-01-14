package com.luzhu.truck.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table
public class JwtSecretToken {
    @Id
    private String userId;
    private String secretKey;
}
