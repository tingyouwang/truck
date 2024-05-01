package com.luzhu.truck.dto.caragency;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCarAgencyParam {
    @NotNull
    private int id;
    @NotNull
    private String agencyName;
    @NotNull
    private String address;
    @NotNull
    private String owner;
    @NotNull
    private String taxId;
    private String phone1;
    private String phone2;
    private String mobile;
    private String fax;
}
