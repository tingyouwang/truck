package com.luzhu.truck.dto.bill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MonthBillDetailReq {
//    @NotNull(message = "車主id不可為空")
//    private Long id;
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotNull(message = "帳單日期不可為空")
    private List<String> billDateList;
    @NotBlank(message = "列印選項不可為空")
    private String print;
//    @NotBlank(message = "車主必填")
//    private String ownerName;
}
