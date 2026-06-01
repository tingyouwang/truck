package com.luzhu.truck.dao.otherlendmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.otherlendmoney.OtherLendMoney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OtherLendMoneyDao extends BaseDao<OtherLendMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `other_lend_money` (`car_license_num`, `lend_date`, `amount`, `type`, `rebill_source_other_lend_money_id`, `rebill_target_other_lend_money_id`, `disable`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
    int insertOtherLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String type,
                             Integer rebillSourceOtherLendMoneyId, Integer rebillTargetOtherLendMoneyId,
                             int disable, String note, long createTime, long lastModifyTime);

    @Query(value = "SELECT SUM(amount) FROM other_lend_money WHERE car_license_num = ?1 AND " +
            "lend_date between ?2 AND ?3 AND disable = 0", nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM other_lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3 " +
            "AND (disable = 0 OR (disable = 1 AND rebill_target_other_lend_money_id IS NOT NULL))"
            , nativeQuery = true)
    List<OtherLendMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Modifying
    @Query(value = "UPDATE `other_lend_money` " +
            "SET `car_license_num` = ?1, `lend_date` = ?2, `amount` = ?3, `disable` = ?4, `note` = ?5, " +
            "`last_modify_time` = ?6 " +
            "WHERE `id` = ?7", nativeQuery = true)
    int updateOtherLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, int disable, String note, long lastModifyTime, long id);

    @Query(value = "SELECT * FROM other_lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3 " +
            "AND (disable = 0 OR (disable = 1 AND rebill_target_other_lend_money_id IS NOT NULL))"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM other_lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3 " +
            "AND (disable = 0 OR (disable = 1 AND rebill_target_other_lend_money_id IS NOT NULL))")
    Page<OtherLendMoney> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE other_lend_money SET disable = 1, rebill_target_other_lend_money_id = ?2, last_modify_time = ?3 " +
            "WHERE id = ?1 AND disable = 0 AND rebill_target_other_lend_money_id IS NULL",
            nativeQuery = true)
    int markOriginalOtherLendMoneyRebilled(int otherLendMoneyId, int newOtherLendMoneyId, long lastModifyTime);
}
