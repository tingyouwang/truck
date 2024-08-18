package com.luzhu.truck.dto.bill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MonthBillReq {
    @NotBlank(message = "車主id不可為空")
    private long id;
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "車主姓名不可為空")
    private String ownerName;
    @NotNull(message = "帳單日期不可為空")
    private String billDate;
}
