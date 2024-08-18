package com.luzhu.truck.dao.fueltax;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.fuel.FuelTax;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;


public interface FuelTaxDao extends BaseDao<FuelTax, String> {
    @Query(value = "SELECT amount FROM fuel_tax WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);

}
