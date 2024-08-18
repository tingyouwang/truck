package com.luzhu.truck.dao.managefee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.laborInsurance.LaborInsurance;
import com.luzhu.truck.entity.managefee.ManageFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ManageFeeDao extends BaseDao<ManageFee, String> {
    @Query(value = "SELECT amount FROM manage_fee WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
}
