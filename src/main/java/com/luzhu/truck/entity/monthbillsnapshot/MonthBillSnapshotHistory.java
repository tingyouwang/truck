package com.luzhu.truck.entity.monthbillsnapshot;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "month_bill_snapshot_history")
@Data
public class MonthBillSnapshotHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_license_num", nullable = false, length = 64)
    private String carLicenseNum;

    @Column(name = "bill_year_month", nullable = false, length = 7)
    private String billYearMonth;

    @Column(name = "changed_at", nullable = false)
    private Long changedAt;

    @Column(name = "snapshot_type", nullable = false, length = 32)
    private String snapshotType;

    @Column(name = "bill_payload_json", nullable = false, columnDefinition = "LONGTEXT")
    private String billPayloadJson;

    @Column(name = "remark", length = 500)
    private String remark;
}
