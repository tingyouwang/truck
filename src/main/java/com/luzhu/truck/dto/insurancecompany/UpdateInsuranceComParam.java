package com.luzhu.truck.dto.insurancecompany;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateInsuranceComParam {
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
