package com.luzhu.truck.dao.loanfeesetting;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanFeeSettingDao extends BaseDao<LoanFeeSetting, Integer> {
    @Query(value = "SELECT * FROM loan_fee_setting WHERE ?1 BETWEEN start_date AND end_date AND status = 'enable'",
    nativeQuery = true)
    List<LoanFeeSetting> getUsingLoanFeeSetting(LocalDate now);
    @Query(value = "SELECT * FROM loan_fee_setting WHERE ?1 BETWEEN start_date AND end_date AND car_license_num = ?2 AND status = 'enable'",
            nativeQuery = true)
    LoanFeeSetting getUsingLoanFeeSettingByCarLicenseNum(LocalDate now, String carLicenseNum);
    @Query(value = "SELECT * FROM loan_fee_setting WHERE car_license_num = ?1",
            nativeQuery = true,
    countQuery = "SELECT COUNT(1) FROM loan_fee_setting WHERE car_license_num = ?1")
    Page<LoanFeeSetting> getAllByCarLicenseNum(String carLicenseNum, Pageable pageable);
    @Modifying
    @Query(value = "INSERT INTO loan_fee_setting (car_license_num, loan_company, start_date, start_datetime, end_date, end_datetime, total_amount, month_pay_amount, status) VALUES " +
            "(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)",
    nativeQuery = true)
    int insertLoanFeeSetting(String carLicenseNum, String loanCom, LocalDate startDate, LocalDateTime startDatetime, LocalDate endDate, LocalDateTime endDatetime, BigDecimal totalAmount, BigDecimal monthPayAmount, String status);
    
    @Modifying
    @Query(value = "UPDATE loan_fee_setting SET status = ?2 WHERE id = ?1", nativeQuery = true)
    int updateLoanFeeSettingStatus(Integer id, String status);
    
    @Modifying
    @Query(value = "UPDATE loan_fee_setting SET car_license_num = ?2, loan_company = ?3, start_date = ?4, " +
            "end_date = ?5, total_amount = ?6, month_pay_amount = ?7, note = ?8, status = ?9 WHERE id = ?1", 
            nativeQuery = true)
    int updateLoanFeeSetting(Integer id, String carLicenseNum, String loanCompany, String startDate, 
                             String endDate, BigDecimal totalAmount, BigDecimal monthPayAmount, 
                             String note, String status);
    
    @Query(value = "SELECT COUNT(1) FROM loan_fee_setting WHERE car_license_num = ?1 AND status = 'enable'", nativeQuery = true)
    int countEnabledByCarLicenseNum(String carLicenseNum);
    
    @Query(value = "SELECT COUNT(1) FROM loan_fee_setting WHERE car_license_num = ?1 AND status = 'enable' AND id != ?2", nativeQuery = true)
    int countEnabledByCarLicenseNumExcludingId(String carLicenseNum, Integer id);

}
