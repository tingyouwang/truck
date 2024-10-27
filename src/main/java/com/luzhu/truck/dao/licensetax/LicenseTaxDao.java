package com.luzhu.truck.dao.licensetax;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.licensetax.LicenseTax;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface LicenseTaxDao extends BaseDao<LicenseTax, String> {
    @Query(value = "SELECT amount FROM license_tax WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
    @Query(value = "SELECT * FROM license_tax WHERE car_license_num = ?1 AND expense_year_month IN (?2)"
            , nativeQuery = true)
    List<LicenseTax> getDetailByExpenseYearMonth(String carLicenseNum, List<String> expenseYearMonth);
}
