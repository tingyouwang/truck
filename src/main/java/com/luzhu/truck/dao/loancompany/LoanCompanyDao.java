package com.luzhu.truck.dao.loancompany;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.loancompany.LoanCompanyDropDownDTO;
import com.luzhu.truck.entity.loancompany.LoanCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanCompanyDao extends BaseDao<LoanCompany, Integer> {
    @Query(value = "SELECT * FROM loan_company WHERE status = 'enable'",
            countQuery = " SELECT COUNT(1) FROM loan_company WHERE status = 'enable'"
            , nativeQuery = true)
    Page<LoanCompany> getAllLoanCompany(Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM loan_company WHERE id = ?1",
            nativeQuery = true)
    int deleteLoanCompanyById(int id);

    @Modifying
    @Query(value = "INSERT INTO loan_company (company_name, short_name, " +
            "contactor, phone, note) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    int insertLoanCompany(String companyName, String shortName, String contactor, String phone, String note);

    @Query(value = "SELECT id, company_name FROM loan_company WHERE status = 'enable'",
            nativeQuery = true)
    List<LoanCompanyDropDownDTO> dropDownGetCarAgency();

    @Modifying
    @Query(value = "UPDATE loan_company SET status = ?2 WHERE id = ?1",
            nativeQuery = true)
    int updateLoanCompanyStatus(int id, String status);

}
