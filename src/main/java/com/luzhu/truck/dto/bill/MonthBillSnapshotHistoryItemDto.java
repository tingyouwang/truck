package com.luzhu.truck.dto.bill;

import lombok.Data;

@Data
public class MonthBillSnapshotHistoryItemDto {
    private Long id;
    /** UTC epoch seconds */
    private Long changedAt;
    private String snapshotType;
    private String remark;
    private MonthBillResponse billData;
}
