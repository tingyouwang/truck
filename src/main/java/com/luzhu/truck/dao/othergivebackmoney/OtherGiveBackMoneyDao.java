package com.luzhu.truck.dao.othergivebackmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OtherGiveBackMoneyDao extends BaseDao<OtherGiveBackMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `other_give_back_money` (`car_license_num`, `give_back_date`, `amount`, `disable`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    int insertOtherGiveBackMoney(String carLicenseNum, String lendDate, BigDecimal amount, int disable, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `other_give_back_money` " +
            "SET `car_license_num` = ?1, `give_back_date` = ?2, `amount` = ?3, `disable` = ?4, `note` = ?5, " +
            "`last_modify_time` = ?6 " +
            "WHERE `id` = ?7", nativeQuery = true)
    int updateOtherGiveBackMoney(String carLicenseNum, String giveBackDate, BigDecimal amount, int disable, String note, long lastModifyTime, long id);


    @Query(value = "SELECT SUM(amount) FROM other_give_back_money WHERE car_license_num = ?1 AND " +
            "give_back_date between ?2 AND ?3 AND disable = 0" , nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3 AND disable = 0"
            , nativeQuery = true)
    List<OtherGiveBackMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3 AND disable = 0"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3 AND disable = 0")
    Page<OtherGiveBackMoney> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);
}
