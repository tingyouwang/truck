package com.luzhu.truck.dao.licensetax;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.licensetax.LicenseTax;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface LicenseTaxDao extends BaseDao<LicenseTax, String> {
    @Query(value = "SELECT amount FROM license_tax WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
}
