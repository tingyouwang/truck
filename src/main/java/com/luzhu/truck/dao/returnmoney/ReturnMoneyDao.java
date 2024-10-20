package com.luzhu.truck.dao.returnmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.returnmoney.ReturnMoney;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ReturnMoneyDao extends BaseDao<ReturnMoney, Integer> {
    @Query(value = "SELECT SUM(amount) FROM return_money WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3",
            nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
}
