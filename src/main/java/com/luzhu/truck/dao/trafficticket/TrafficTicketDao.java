package com.luzhu.truck.dao.trafficticket;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface TrafficTicketDao extends BaseDao<TrafficTicket, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `traffic_ticket` (`car_license_num`, `handle_date`, `ticket_date`, `go_police_date`, `pay_date`, `ticket_num`, `amount`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
    int insertTicket(String carLicenseNum, String handleDate, String ticketDate, String goPoliceDate, String payDate, String ticketNum
                        ,BigDecimal amount, String note, long createTime, long lastModifyTime);

    @Query(value = "SELECT SUM(amount) FROM traffic_ticket WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3" , nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
}
