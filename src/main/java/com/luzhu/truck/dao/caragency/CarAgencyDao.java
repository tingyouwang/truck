package com.luzhu.truck.dao.caragency;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.caragency.CarAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarAgencyDao extends BaseDao<CarAgency, Integer> {
    @Query(value = "SELECT * FROM car_agency",
            countQuery = " SELECT COUNT(1) FROM car_agency"
            , nativeQuery = true)
    Page<CarAgency> getAllCarAgency(Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO car_agency (agency_name, address, " +
            "owner, tax_id, phone1, phone2, mobile, fax, agency_short_name) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
    int insertCarAgency(String agencyName, String address, String owner, String taxId, String phone1, String phone2, String mobile, String fax, String shortName);

    @Modifying
    @Query(value = "DELETE FROM car_agency WHERE id = ?1",
    nativeQuery = true)
    int deleteCarAgencyById(int id);


}
