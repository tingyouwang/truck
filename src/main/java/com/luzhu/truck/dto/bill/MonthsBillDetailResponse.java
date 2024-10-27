package com.luzhu.truck.dto.bill;

import lombok.Data;

import java.util.List;

@Data
public class MonthsBillDetailResponse {
    private List<MonthsBillDetailDto> detailDtos;
    private Integer sum;
    private Integer receiveSum;
    private Integer offsetSum;
}
