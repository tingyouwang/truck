package com.luzhu.truck.dao.othergivebackmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OtherGiveBackMoneyDao extends BaseDao<OtherGiveBackMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `other_give_back_money` (`car_license_num`, `give_back_date`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertOtherGiveBackMoney(String carLicenseNum, String lendDate, BigDecimal amount, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `other_give_back_money` " +
            "SET `car_license_num` = ?1, `give_back_date` = ?2, `amount` = ?3, `note` = ?4, " +
            "`last_modify_time` = ?5 " +
            "WHERE `id` = ?6", nativeQuery = true)
    int updateOtherGiveBackMoney(String carLicenseNum, String giveBackDate, BigDecimal amount, String note, long lastModifyTime, long id);


    @Query(value = "SELECT SUM(amount) FROM other_give_back_money WHERE car_license_num = ?1 AND " +
            "give_back_date between ?2 AND ?3" , nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3"
            , nativeQuery = true)
    List<OtherGiveBackMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
}
