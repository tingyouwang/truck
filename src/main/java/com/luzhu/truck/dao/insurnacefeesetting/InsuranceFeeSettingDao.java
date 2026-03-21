package com.luzhu.truck.dao.insurnacefeesetting;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.insurancefeesetting.DeleteInsuranceSettingParam;
import com.luzhu.truck.entity.insurancecompany.InsuranceCompany;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "INSERT INTO insurance_fee_setting (car_license_num, insurance_com, start_date, end_date, pay_us_date" +
            ", amount, insurance_type, insurance_num, insurance_card_num, quit_date, create_time, update_time) VALUES " +
            "(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12)",
    nativeQuery = true)
    @Modifying
    int insertInsuranceFeeSetting(String carLicenseNum, String insuranceCom, String startDate, String endDate,
                                  String payUsDate, double amount, String insuranceType, String insuranceNum,
                                  String insuranceCardNum, String quitDate, long createTime, long updateTime);
    @Query(value = "SELECT * FROM insurance_fee_setting WHERE car_license_num = ?1 ORDER BY start_date DESC",
            countQuery = " SELECT COUNT(1) FROM insurance_fee_setting WHERE car_license_num = ?1 ORDER BY start_date DESC"
            , nativeQuery = true)
    Page<InsuranceFeeSetting> getInsuranceFeeSettingByLicenseNum(String carLicenseNum, Pageable pageable);

    @Query(value = "SELECT * FROM insurance_fee_setting WHERE car_license_num = ?1 AND insurance_card_num = ?2",
            nativeQuery = true)
    InsuranceFeeSetting getInsuranceFeeSettingByPk(String carLicenseNum, String insuranceCardNum);

    @Modifying
    @Query(value = "UPDATE insurance_fee_setting SET status = 'DISABLE' WHERE car_license_num = ?1 AND insurance_card_num = ?2",
            nativeQuery = true)
    int updateInsuranceFeeSettingById(String carLicenseNum, String insuranceCardNum);

    @Query(value = "SELECT * FROM insurance_fee_setting WHERE ?1 BETWEEN start_date AND end_date AND status = 'ENABLE' AND generate_bill = 'N'",
            nativeQuery = true)
    List<InsuranceFeeSetting> getUsingInsuranceFeeSetting(LocalDate now);

    @Query(value = "SELECT * FROM insurance_fee_setting WHERE ?1 BETWEEN start_date AND end_date AND status = 'ENABLE' AND generate_bill = 'N' AND car_license_num = ?2",
            nativeQuery = true)
    InsuranceFeeSetting getInsuranceFeeSettingByCarNum(LocalDate now, String carNum);

    @Modifying
    @Query(value = "UPDATE insurance_fee_setting " +
            "SET insurance_com = ?2, start_date = ?3, end_date = ?4, pay_us_date = ?5, " +
            "amount = ?6, insurance_type = ?7, insurance_num = ?8, quit_date = ?9, " +
            "update_time = ?10, update_by = ?11, status = ?12 " +
            "WHERE car_license_num = ?1 AND insurance_card_num = ?13",
            nativeQuery = true)
    int updateInsuranceFeeSetting(String carLicenseNum, String insuranceCom, String startDate, String endDate,
                                  String payUsDate, double amount, String insuranceType, String insuranceNum,
                                  String quitDate, long updateTime, String updateBy, String status,
                                  String OriginalInsuranceCardNum);

    @Modifying
    @Query(value = "UPDATE insurance_fee_setting " +
            "SET status = ?2, update_time = ?4" +
            " WHERE car_license_num = ?1 AND insurance_card_num = ?3",
            nativeQuery = true)
    int updateInsuranceFeeSettingStatus(String carLicenseNum, String status, String insuranceCardNum, long updateTime);
}
