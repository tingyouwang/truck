package com.luzhu.truck.dao.invoice;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.invoice.InvoiceSumAmountAndTaxDto;
import com.luzhu.truck.entity.invoice.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceDao extends BaseDao<Invoice, Integer> {
    @Modifying
    @Query(value = "INSERT INTO invoice (invoice_num, invoice_date, " +
            "handle_date, amount, amount_tax, car_agency, car_agency_num, disable, note, tax_month, car_license_num, type) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12)", nativeQuery = true)
    int insertInvoice(String invoiceNum, String invoiceDate, String handleDate, BigDecimal amount, BigDecimal amountTax,
                      String carAgency, int carAgencyNum, int disable, String note, String taxMonth, String carLicenseNum
    , String type);

    @Query(value = "SELECT amount, amount_tax FROM invoice WHERE car_license_num = ?1 AND " +
            "invoice_date between ?2 AND ?3" +
            " AND TYPE = ?4", nativeQuery = true)
    List<Invoice> getVoiceByType(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, String type);

    @Query(value = "SELECT SUM(amount) AS sum, SUM(amount_tax) TaxSum FROM invoice WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3" +
            " AND TYPE = ?4", nativeQuery = true)
    InvoiceSumAmountAndTaxDto getSumAmountByType(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, String type);

    @Query(value = "SELECT * FROM invoice WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3" +
            " AND TYPE = ?4", nativeQuery = true)
    Page<Invoice> getAllByType(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, String type, Pageable pageable);

}
