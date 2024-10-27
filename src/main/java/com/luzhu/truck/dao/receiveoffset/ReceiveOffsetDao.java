package com.luzhu.truck.dao.receiveoffset;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.receiveoffset.ReceiveOffset;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//收據底收
@Repository
public interface ReceiveOffsetDao extends BaseDao<ReceiveOffset, Integer> {
    @Query(value = "SELECT SUM(amount) FROM receive_offset WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3",
            nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM receive_offset WHERE car_license_num = ?1 AND pay_date between ?2 AND ?3"
            , nativeQuery = true)
    List<ReceiveOffset> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
}
