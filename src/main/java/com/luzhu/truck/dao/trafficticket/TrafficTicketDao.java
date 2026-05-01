package com.luzhu.truck.dao.trafficticket;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrafficTicketDao extends BaseDao<TrafficTicket, Integer> {
    @Modifying
    @Query(value = "INSERT INTO `traffic_ticket` (`car_license_num`, `handle_date`, `ticket_date`, `go_police_date`, `pay_date`, `ticket_num`, `amount`, `disable`, `note`, `create_time`, `last_modify_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11)", nativeQuery = true)
    int insertTicket(String carLicenseNum, String handleDate, String ticketDate, String goPoliceDate, String payDate, String ticketNum
                        ,BigDecimal amount, int disable, String note, long createTime, long lastModifyTime);

    @Modifying
    @Query(value = "UPDATE `traffic_ticket` " +
            "SET `car_license_num` = ?1, `handle_date` = ?2, `ticket_date` = ?3, " +
            "`go_police_date` = ?4, `pay_date` = ?5, `ticket_num` = ?6, " +
            "`amount` = ?7, `disable` = ?8, `note` = ?9, `last_modify_time` = ?10 " +
            "WHERE `id` = ?11", nativeQuery = true)
    int updateTicket(String carLicenseNum, String handleDate, String ticketDate, String goPoliceDate,
                     String payDate, String ticketNum, BigDecimal amount, int disable, String note, long lastModifyTime, long id);


    @Query(value = "SELECT SUM(amount) FROM traffic_ticket WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3 AND disable = 0" , nativeQuery = true)
    BigDecimal getSumAmount(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);
    @Query(value = "SELECT * FROM traffic_ticket WHERE car_license_num = ?1 AND handle_date between ?2 AND ?3 AND disable = 0"
            , nativeQuery = true)
    List<TrafficTicket> getDetailByDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate);

    @Query(value = "SELECT * FROM traffic_ticket WHERE car_license_num = ?1 AND handle_date between ?2 AND ?3 AND disable = 0"
            , nativeQuery = true
            , countQuery = "SELECT COUNT(1) FROM traffic_ticket WHERE car_license_num = ?1 AND handle_date between ?2 AND ?3 AND disable = 0")
    Page<TrafficTicket> getList(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, Pageable pageable);

}
