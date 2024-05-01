package com.luzhu.truck.dto.loancompany;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateLoanComParam {
    @NotNull
    private int id;
    @NotNull
    private String companyName;
    @NotNull
    private String shortName;
    @NotNull
    private String contactor;
    @NotNull
    private String phone;
    @NotNull
    private String note;
}
