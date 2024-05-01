package com.luzhu.truck.dto.loancompany;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddLoadComParam {
    @NotBlank(message = "貸款公司名稱不可為空")
    private String companyName;
    private String shortName;
    @NotBlank
    private String contactor;
    @NotBlank
    private String phone;
    private String note;
}
