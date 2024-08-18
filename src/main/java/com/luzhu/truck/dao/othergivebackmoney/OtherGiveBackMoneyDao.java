package com.luzhu.truck.dao.othergivebackmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface OtherGiveBackMoneyDao extends BaseDao<OtherGiveBackMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `other_give_back_money` (`car_license_num`, `give_back_date`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertOtherGiveBackMoney(String carLicenseNum, String lendDate, BigDecimal amount, String note, long createTime, long lastModifyTime);
}
