package com.luzhu.truck.dto.insurancecompany;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddInsuranceComParam {
    @NotBlank(message = "保險公司名稱不可為空")
    private String companyName;
    private String shortName;
    @NotBlank
    private String contactor;
    @NotBlank
    private String phone;
    private String note;
}
