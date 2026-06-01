package com.luzhu.truck.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VoidAndRebillToMonthParam {
    @NotNull
    private Integer id;
    @NotBlank(message = "目標帳單月份不可為空")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "目標月份格式應為 yyyy-MM")
    private String targetBillYearMonth;
    /** 寫入新調整列的備註（可空） */
    private String note;
}
