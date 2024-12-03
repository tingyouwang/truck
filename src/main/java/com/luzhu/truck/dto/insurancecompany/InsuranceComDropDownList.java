package com.luzhu.truck.dto.insurancecompany;

import com.luzhu.truck.annotation.JpaDto;
import lombok.Data;

@Data
@JpaDto
public class InsuranceComDropDownList {
    private long id;
    private String companyName;
}
