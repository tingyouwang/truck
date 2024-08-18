package com.luzhu.truck.dao.loanfee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.loanfee.LoanFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface LoanFeeDao extends BaseDao<LoanFee, String> {
    @Query(value = "SELECT amount FROM loan_fee" +
            " WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
}
