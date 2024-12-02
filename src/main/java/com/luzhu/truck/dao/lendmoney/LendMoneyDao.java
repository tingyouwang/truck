package com.luzhu.truck.dao.lendmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.lendmoney.SumAmountAndTaxDto;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LendMoneyDao extends BaseDao<LendMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `lend_money` (`car_license_num`, `lend_date`, `amount`, `type`, `expire_date`, `interest_amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
    int insertLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String type,
                      String expireDate, BigDecimal interestAmount, String note, long createTime, long lastModifyTime);

    @Query(value = "SELECT SUM(amount) AS sum, SUM(interest_amount) AS interestSum FROM lend_money WHERE car_license_num = ?1 AND " +
            "lend_date between ?2 AND ?3" , nativeQuery = true)
    SumAmountAndTaxDto getLendMoney(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3"
            , nativeQuery = true)
    List<LendMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Modifying
    @Query(value = "UPDATE `lend_money` " +
            "SET `car_license_num` = ?1, `lend_date` = ?2, `amount` = ?3, `type` = ?4, " +
            "`expire_date` = ?5, `interest_amount` = ?6, `note` = ?7, `last_modify_time` = ?8 " +
            "WHERE `id` = ?9", nativeQuery = true)
    int updateLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String type,
                        String expireDate, BigDecimal interestAmount, String note, long lastModifyTime, long id);

}
