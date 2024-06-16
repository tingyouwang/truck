package com.luzhu.truck.dao.insurnacefeesetting;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsuranceFeeSettingDao extends BaseDao<InsuranceFeeSetting, String> {
//    @Query(value = "SELECT * FROM insurance_fee_setting WHERE ?1 BETWEEN start_date AND end_date",
//    nativeQuery = true)
//    List<LoanFeeSetting> getUsingLoanFeeSetting(LocalDate now);
    @Query(value = "INSERT INTO insurance_fee_setting (car_license_num, insurance_com, start_date, end_date, amount" +
            ", insurance_type, insurance_num, insurance_card_num, create_time, update_time) VALUES " +
            "(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)",
    nativeQuery = true)
    @Modifying
    int insertInsuranceFeeSetting(String carLicenseNum, String insuranceCom, LocalDate startDate, LocalDate endDate,
                                  double amount, String insuranceType, String insuranceNum, String insuranceCardNum, long createTime,
                                  long updateTime);

}
