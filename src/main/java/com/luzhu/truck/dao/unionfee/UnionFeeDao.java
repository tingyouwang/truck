package com.luzhu.truck.dao.unionfee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.managefee.ManageFee;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UnionFeeDao extends BaseDao<UnionFee, String> {
    @Query(value = "SELECT amount FROM union_fee WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
    @Query(value = "SELECT * FROM union_fee WHERE car_license_num = ?1 AND expense_year_month IN (?2)"
            , nativeQuery = true)
    List<UnionFee> getDetailByExpenseYearMonth(String carLicenseNum, List<String> expenseYearMonth);
}
