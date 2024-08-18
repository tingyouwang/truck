package com.luzhu.truck.dao.otherlendmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.otherlendmoney.OtherLendMoney;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OtherLendMoneyDao extends BaseDao<OtherLendMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `other_lend_money` (`car_license_num`, `lend_date`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertOtherLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String note, long createTime, long lastModifyTime);
}
