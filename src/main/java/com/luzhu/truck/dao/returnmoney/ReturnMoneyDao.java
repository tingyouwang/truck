package com.luzhu.truck.dao.returnmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.returnmoney.ReturnMoney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReturnMoneyDao extends BaseDao<ReturnMoney, Integer> {
    @Query(value = "SELECT SUM(amount) FROM return_money WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3",
            nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM return_money WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3"
            , nativeQuery = true)
    List<ReturnMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Modifying
    @Query(value = "INSERT INTO `return_money` (`car_license_num`, `pay_date`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertReturnMoney(String carLicenseNum, String payDate, BigDecimal amount,
                          String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `return_money` " +
            "SET `car_license_num` = ?1, `pay_date` = ?2," +
            "`amount` = ?3, `note` = ?4, `last_modify_time` = ?5 " +
            "WHERE `id` = ?6", nativeQuery = true)
    int updateReturnMoney(String carLicenseNum, String payDate,
                          BigDecimal amount, String note, long lastModifyTime, long id);

    @Query(value = "SELECT * FROM return_money WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM return_money WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3")
    Page<ReturnMoney> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);


}
