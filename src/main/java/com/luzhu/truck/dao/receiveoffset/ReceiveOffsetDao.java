package com.luzhu.truck.dao.receiveoffset;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.receiveoffset.ReceiveOffset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//收據底收
@Repository
public interface ReceiveOffsetDao extends BaseDao<ReceiveOffset, Integer> {
    @Query(value = "SELECT SUM(amount) FROM receive_offset WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3 AND disable = 0",
            nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM receive_offset WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3 AND disable = 0"
            , nativeQuery = true)
    List<ReceiveOffset> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM receive_offset WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3 AND disable = 0"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM receive_offset WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3 AND disable = 0")
    Page<ReceiveOffset> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO `receive_offset` (`car_license_num`, `pay_date`, `amount`, `receipt_amount`, `disable`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
    int insertReceiveOffset(String carLicenseNum, String payDate, BigDecimal amount,
                            BigDecimal receiptAmount, int disable, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `receive_offset` " +
            "SET `car_license_num` = ?1, `pay_date` = ?2, " +
            "`amount` = ?3, `receipt_amount` = ?4, `disable` = ?5, `note` = ?6, `last_modify_time` = ?7 " +
            "WHERE `id` = ?8", nativeQuery = true)
    int updateReceiveOffset(String carLicenseNum, String payDate,
                            BigDecimal amount, BigDecimal receiptAmount, int disable, String note,
                            long lastModifyTime, long id);


}
