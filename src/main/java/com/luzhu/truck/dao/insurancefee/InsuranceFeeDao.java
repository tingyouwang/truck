package com.luzhu.truck.dao.insurancefee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.insurancefee.InsuranceFee;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsuranceFeeDao extends BaseDao<InsuranceFee, String> {
    @Query(value = "SELECT amount FROM insurance_fee WHERE car_license_num = ?1 AND " +
            "expense_year_month = ?2", nativeQuery = true)
    BigDecimal getFeeById(String carLicenseNum, String expenseYearMonth);
    @Query(value = "SELECT * FROM insurance_fee WHERE car_license_num = ?1 AND expense_year_month IN (?2)"
            , nativeQuery = true)
    List<InsuranceFee> getDetailByExpenseYearMonth(String carLicenseNum, List<String> expenseYearMonth);
    @Modifying
    @Query(value = "UPDATE insurance_fee SET status = 'DISABLE' WHERE car_license_num = ?1 AND insurance_card_num = ?2",
            nativeQuery = true)
    int disableInsuranceFee(String carLicenseNum, String insuranceCardNum);

    @Modifying
    @Query(value = "UPDATE insurance_fee SET amount = ?3, insurance_card_num = ?4, expense_year_month = ?5 WHERE car_license_num = ?1 AND insurance_card_num = ?2",
            nativeQuery = true)
    int updateInsuranceFee(String carLicenseNum, String originalInsuranceCardNum, Double amount, String insuranceCardNum, String yearMonth);


}
