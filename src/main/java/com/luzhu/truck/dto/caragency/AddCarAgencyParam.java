package com.luzhu.truck.dto.caragency;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCarAgencyParam {
    @NotBlank
    private String agencyName;
    @NotBlank
    private String agencyShortName;
    @NotBlank
    private String address;
    @NotBlank
    private String owner;
    @NotBlank
    private String taxId;
    private String phone1;
    private String phone2;
    private String mobile;
    private String fax;
}
