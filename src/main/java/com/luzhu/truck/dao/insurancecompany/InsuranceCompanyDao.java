package com.luzhu.truck.dao.insurancecompany;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.insurancecompany.InsuranceComDropDownList;
import com.luzhu.truck.entity.insurancecompany.InsuranceCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceCompanyDao extends BaseDao<InsuranceCompany, Integer> {
    @Query(value = "SELECT * FROM insurance_company",
            countQuery = " SELECT COUNT(1) FROM insurance_company"
            , nativeQuery = true)
    Page<InsuranceCompany> getAllInsuranceCompany(Pageable pageable);

    @Query(value = "SELECT id, company_name FROM insurance_company"
            , nativeQuery = true)
    List<InsuranceComDropDownList> getInsuranceComDropDown();

    @Modifying
    @Query(value = "INSERT INTO insurance_company (company_name, short_name, " +
            "contactor, phone, note) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    int insertInsuranceCom(String companyName, String shortName, String contactor, String phone, String note);

    @Modifying
    @Query(value = "DELETE FROM insurance_company WHERE id = ?1",
            nativeQuery = true)
    int deleteInsuranceCompanyById(int id);
}
