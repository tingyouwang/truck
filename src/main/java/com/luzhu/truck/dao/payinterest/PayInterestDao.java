package com.luzhu.truck.dao.payinterest;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.payinterest.PayInterest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//代支利息
@Repository
public interface PayInterestDao extends BaseDao<PayInterest, Integer> {
    @Query(value = "SELECT SUM(amount) FROM pay_interest WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3",
    nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM pay_interest WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3"
            , nativeQuery = true)
    List<PayInterest> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Modifying
    @Query(value = "INSERT INTO `pay_interest` (`car_license_num`, `pay_date`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertPayInterest(String carLicenseNum, String payDate, BigDecimal amount,
                          String note, long createTime, long lastModifyTime);

}
