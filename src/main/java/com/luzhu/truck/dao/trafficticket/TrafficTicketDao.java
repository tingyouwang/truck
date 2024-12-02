package com.luzhu.truck.dao.trafficticket;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrafficTicketDao extends BaseDao<TrafficTicket, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `traffic_ticket` (`car_license_num`, `handle_date`, `ticket_date`, `go_police_date`, `pay_date`, `ticket_num`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
    int insertTicket(String carLicenseNum, String handleDate, String ticketDate, String goPoliceDate, String payDate, String ticketNum
                        ,BigDecimal amount, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `traffic_ticket` " +
            "SET `car_license_num` = ?1, `handle_date` = ?2, `ticket_date` = ?3, " +
            "`go_police_date` = ?4, `pay_date` = ?5, `ticket_num` = ?6, " +
            "`amount` = ?7, `note` = ?8, `last_modify_time` = ?9 " +
            "WHERE `id` = ?10", nativeQuery = true)
    int updateTicket(String carLicenseNum, String handleDate, String ticketDate, String goPoliceDate,
                     String payDate, String ticketNum, BigDecimal amount, String note, long lastModifyTime, long id);


    @Query(value = "SELECT SUM(amount) FROM traffic_ticket WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3" , nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM traffic_ticket WHERE car_license_num = ?1 AND handle_date between ?2 AND ?3"
            , nativeQuery = true)
    List<TrafficTicket> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

}
