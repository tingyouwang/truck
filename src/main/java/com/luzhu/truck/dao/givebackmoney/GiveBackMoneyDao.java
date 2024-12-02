package com.luzhu.truck.dao.givebackmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.givebackmoney.SumGiveBackMoneyAmountAndInterestDto;
import com.luzhu.truck.dto.lendmoney.SumAmountAndTaxDto;
import com.luzhu.truck.entity.givebackmoney.GiveBackMoney;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GiveBackMoneyDao extends BaseDao<GiveBackMoney, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `give_back_money` (`car_license_num`, `give_back_date`, `amount`, `type`, `expire_date`, `interest_amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
    int insertLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String type,
                        String expireDate, BigDecimal interestAmount, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `give_back_money` " +
            "SET `car_license_num` = ?1, `give_back_date` = ?2, `amount` = ?3, `type` = ?4, " +
            "`expire_date` = ?5, `interest_amount` = ?6, `note` = ?7, `last_modify_time` = ?8 " +
            "WHERE `id` = ?9", nativeQuery = true)
    int updateGiveBackMoney(String carLicenseNum, String giveBackDate, BigDecimal amount, String type,
                            String expireDate, BigDecimal interestAmount, String note, long lastModifyTime, long id);


    @Query(value = "SELECT SUM(amount) AS sum, SUM(interest_amount) AS interestSum FROM give_back_money WHERE car_license_num = ?1 AND " +
            "give_back_date between ?2 AND ?3" , nativeQuery = true)
    SumGiveBackMoneyAmountAndInterestDto getGiveBackMoney(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3"
            , nativeQuery = true)
    List<GiveBackMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM give_back_money WHERE car_license_num = ?1 AND give_back_date between ?2 AND ?3")
    Page<GiveBackMoney> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);
}
