package com.luzhu.truck.dao.monthbillsnapshot;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.monthbillsnapshot.MonthBillSnapshotHistory;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonthBillSnapshotHistoryDao extends BaseDao<MonthBillSnapshotHistory, Long> {

    @Query(value = "SELECT * FROM month_bill_snapshot_history WHERE car_license_num = ?1 AND " +
            "bill_year_month = ?2 ORDER BY changed_at ASC, id ASC", nativeQuery = true)
    List<MonthBillSnapshotHistory> findByCarAndMonthOrderByChangedAtAsc(String carLicenseNum, String billYearMonth);
}
