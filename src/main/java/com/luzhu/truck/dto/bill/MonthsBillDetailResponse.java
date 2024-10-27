package com.luzhu.truck.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class MonthsBillDetailResponse {
    private List<MonthsBillDetailDto> detailDtos;
    private Double sum;
}
