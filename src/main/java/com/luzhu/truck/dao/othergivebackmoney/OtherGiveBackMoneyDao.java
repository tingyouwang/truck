package com.luzhu.truck.dao.othergivebackmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OtherGiveBackMoneyDao extends BaseDao<OtherGiveBackMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `other_give_back_money` (`car_license_num`, `give_back_date`, `amount`, `type`, `rebill_source_other_give_back_money_id`, `rebill_target_other_give_back_money_id`, `disable`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
    int insertOtherGiveBackMoney(String carLicenseNum, String giveBackDate, BigDecimal amount, String type,
                                 Integer rebillSourceOtherGiveBackMoneyId, Integer rebillTargetOtherGiveBackMoneyId,
                                 int disable, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `other_give_back_money` " +
            "SET `car_license_num` = ?1, `give_back_date` = ?2, `amount` = ?3, `disable` = ?4, `note` = ?5, " +
            "`last_modify_time` = ?6 " +
            "WHERE `id` = ?7", nativeQuery = true)
    int updateOtherGiveBackMoney(String carLicenseNum, String giveBackDate, BigDecimal amount, int disable, String note, long lastModifyTime, long id);

    @Query(value = "SELECT SUM(amount) FROM other_give_back_money WHERE car_license_num = ?1 AND " +
            "give_back_date between ?2 AND ?3 AND disable = 0", nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3 " +
            "AND (disable = 0 OR (disable = 1 AND rebill_target_other_give_back_money_id IS NOT NULL))"
            , nativeQuery = true)
    List<OtherGiveBackMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3 " +
            "AND (disable = 0 OR (disable = 1 AND rebill_target_other_give_back_money_id IS NOT NULL))"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM other_give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3 " +
            "AND (disable = 0 OR (disable = 1 AND rebill_target_other_give_back_money_id IS NOT NULL))")
    Page<OtherGiveBackMoney> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE other_give_back_money SET disable = 1, rebill_target_other_give_back_money_id = ?2, last_modify_time = ?3 " +
            "WHERE id = ?1 AND disable = 0 AND rebill_target_other_give_back_money_id IS NULL",
            nativeQuery = true)
    int markOriginalOtherGiveBackMoneyRebilled(int otherGiveBackMoneyId, int newOtherGiveBackMoneyId, long lastModifyTime);
}
