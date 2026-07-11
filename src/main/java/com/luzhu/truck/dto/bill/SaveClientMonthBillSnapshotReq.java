package com.luzhu.truck.dto.bill;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveClientMonthBillSnapshotReq {
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;

    @NotBlank(message = "帳單日期不可為空")
    private String billDate;

    /** 前端帶入的帳單各欄位；totalSum 由後端依組成欄位重新計算後寫入快照，不採前端帶入的值 */
    @NotNull(message = "帳單明細不可為空")
    @Valid
    private MonthBillResponse billData;

    /** 選填，寫入歷史表 remark（例如修改說明） */
    private String remark;
}
