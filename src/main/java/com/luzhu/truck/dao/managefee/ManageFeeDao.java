package com.luzhu.truck.dao.managefee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.laborInsurance.LaborInsurance;
import com.luzhu.truck.entity.managefee.ManageFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ManageFeeDao extends BaseDao<ManageFee, String> {
    @Query(value = "SELECT amount FROM manage_fee WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);

    @Query(value = "SELECT * FROM manage_fee WHERE car_license_num = ?1 AND " +
            "expense_year_month IN (?2)", nativeQuery = true)
    List<ManageFee> getDetailByExpenseYearMonth(String carLicenseNum, List<String> expenseYearMonth);
}
