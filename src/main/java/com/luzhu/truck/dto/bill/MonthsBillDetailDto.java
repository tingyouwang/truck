package com.luzhu.truck.dto.bill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthsBillDetailDto {
    private String expenseYearMonth;
    private String date;
    // 費用名稱
    private String name;
    //應收金額
    @Builder.Default
    private Integer receiveAmount = 0;
    //抵收金額
    @Builder.Default
    private Integer offsetAmount = 0;
    private String note;
}
