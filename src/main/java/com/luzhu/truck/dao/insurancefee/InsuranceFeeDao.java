package com.luzhu.truck.dao.insurancefee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.insurancefee.InsuranceFee;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface InsuranceFeeDao extends BaseDao<InsuranceFee, String> {
    @Query(value = "SELECT amount FROM insurance_fee WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
}
