package com.luzhu.truck.dto.bill;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MonthsBillDetailDto {
    private String expenseYearMonth;
    private String date;
    // 費用名稱
    private String name;
    //應收金額
    @Builder.Default
    private BigDecimal receiveAmount = BigDecimal.ZERO;
    //抵收金額
    @Builder.Default
    private BigDecimal offsetAmount = BigDecimal.ZERO;
    private String note;
}
