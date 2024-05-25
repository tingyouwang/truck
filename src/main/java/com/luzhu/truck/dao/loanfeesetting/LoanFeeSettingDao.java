package com.luzhu.truck.dao.loanfeesetting;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.entity.managefee.ManageFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanFeeSettingDao extends BaseDao<LoanFeeSetting, String> {
    @Query(value = "SELECT * FROM loan_fee_setting WHERE ?1 BETWEEN start_date AND end_date",
    nativeQuery = true)
    List<LoanFeeSetting> getUsingLoanFeeSetting(LocalDate now);
}
