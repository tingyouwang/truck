package com.luzhu.truck.dao.caragency;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.caragency.CarAgencyDropDownDTO;
import com.luzhu.truck.entity.caragency.CarAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarAgencyDao extends BaseDao<CarAgency, Integer> {
    @Query(value = "SELECT * FROM car_agency WHERE status = 'enable'",
            countQuery = " SELECT COUNT(1) FROM car_agency WHERE status = 'enable'"
            , nativeQuery = true)
    Page<CarAgency> getAllCarAgency(Pageable pageable);

    @Query(value = "SELECT id as carAgencyId, agency_name FROM car_agency WHERE status = 'enable'",
            nativeQuery = true)
    List<CarAgencyDropDownDTO> dropDownGetCarAgency();

    @Modifying
    @Query(value = "INSERT INTO car_agency (agency_name, address, " +
            "owner, tax_id, phone1, phone2, mobile, fax) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
    int insertCarAgency(String agencyName, String address, String owner, String taxId, String phone1, String phone2, String mobile, String fax);

    @Modifying
    @Query(value = "DELETE FROM car_agency WHERE id = ?1",
    nativeQuery = true)
    int deleteCarAgencyById(int id);

    @Query(value = "SELECT COUNT(1) FROM car_agency WHERE agency_name = ?1 AND status = 'enable'"
            , nativeQuery = true)
    int countByAgencyName(String agencyName);

    @Query(value = "SELECT COUNT(1) FROM car_agency WHERE agency_name = ?1 AND id != ?2 AND status = 'enable'"
            , nativeQuery = true)
    int countByAgencyName(String agencyName, int id);

    @Modifying
    @Query(value = "UPDATE car_agency SET status = ?2 WHERE id = ?1",
            nativeQuery = true)
    int updateCarAgencyStatus(int id, String status);

}
