package com.luzhu.truck.dao.monthbillsnapshot;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.monthbillsnapshot.MonthBillSnapshot;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MonthBillSnapshotDao extends BaseDao<MonthBillSnapshot, Long> {

    @Query(value = "SELECT * FROM month_bill_snapshot WHERE car_license_num = ?1 AND " +
            "bill_year_month = ?2 LIMIT 1", nativeQuery = true)
    Optional<MonthBillSnapshot> findByCarAndMonth(String carLicenseNum, String billYearMonth);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM month_bill_snapshot WHERE car_license_num = ?1 AND " +
            "bill_year_month = ?2)", nativeQuery = true)
    boolean existsByCarAndMonth(String carLicenseNum, String billYearMonth);

    /**
     * 找出該車在指定月份「之後」已存在的快照（由舊到新），用於歷史帳單修改後的連動調整
     */
    @Query(value = "SELECT * FROM month_bill_snapshot WHERE car_license_num = ?1 AND " +
            "bill_year_month > ?2 ORDER BY bill_year_month ASC", nativeQuery = true)
    List<MonthBillSnapshot> findByCarAndMonthAfterOrderByMonthAsc(String carLicenseNum, String billYearMonth);
}



