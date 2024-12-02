package com.luzhu.truck.dao.otherlendmoney;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.invoice.Invoice;
import com.luzhu.truck.entity.lendmoney.LendMoney;
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
    @Query(value = "INSERT INTO `other_lend_money` (`car_license_num`, `lend_date`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int insertOtherLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String note, long createTime, long lastModifyTime);

    @Query(value = "SELECT SUM(amount) FROM other_lend_money WHERE car_license_num = ?1 AND " +
            "lend_date between ?2 AND ?3" , nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM other_lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3"
            , nativeQuery = true)
    List<OtherLendMoney> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Modifying
    @Query(value = "UPDATE `other_lend_money` " +
            "SET `car_license_num` = ?1, `lend_date` = ?2, `amount` = ?3, `note` = ?4, " +
            "`last_modify_time` = ?5 " +
            "WHERE `id` = ?6", nativeQuery = true)
    int updateOtherLendMoney(String carLicenseNum, String lendDate, BigDecimal amount, String note, long lastModifyTime, long id);

    @Query(value = "SELECT * FROM other_lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM other_lend_money WHERE car_license_num = ?1 AND lend_date between ?2 AND ?3")
    Page<OtherLendMoney> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);
}
