package com.luzhu.truck.dao.laborinsurance;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.laborInsurance.LaborInsurance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface LaborInsuranceDao extends BaseDao<LaborInsurance, String> {
    @Query(value = "SELECT amount FROM labor_insurance WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
}
