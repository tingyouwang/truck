package com.luzhu.truck.dao.lastmonthowe;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.lastmonthowe.LastMonthOwe;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface LastMonthOweDao extends BaseDao<LastMonthOwe, String> {
    @Query(value = "SELECT amount FROM last_month_owe WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getAmountById(String carLicenseNum, String expenseYearMonth);

}
