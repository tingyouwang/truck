package com.luzhu.truck.dto.loancompany;

import com.luzhu.truck.annotation.JpaDto;
import lombok.Data;

@Data
@JpaDto
public class LoanCompanyDropDownDTO {
    private int id;
    private String companyName;
}
