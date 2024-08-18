package com.luzhu.truck.dao.givebackmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.givebackmoney.GiveBackMoney;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface GiveBackMoneyDao extends BaseDao<GiveBackMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `give_back_money` (`car_license_num`, `give_back_date`, `amount`, `type`, `expire_date`, `interest_amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
    int insertLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String type,
                        String expireDate, BigDecimal interestAmount, String note, long createTime, long lastModifyTime);
}
