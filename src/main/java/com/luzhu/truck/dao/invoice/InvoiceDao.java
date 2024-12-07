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
            "handle_date, amount, amount_tax, car_agency, car_agency_num, disable, note, tax_month, car_license_num, type, last_modify_time) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)", nativeQuery = true)
    int insertInvoice(String invoiceNum, String invoiceDate, String handleDate, BigDecimal amount, BigDecimal amountTax,
                      String carAgency, int carAgencyNum, int disable, String note, String taxMonth, String carLicenseNum
    , String type, long now);

    @Query(value = "SELECT amount, amount_tax FROM invoice WHERE car_license_num = ?1 AND " +
            "invoice_date between ?2 AND ?3" +
            " AND TYPE = ?4", nativeQuery = true)
    List<Invoice> getVoiceByType(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, String type);

    @Query(value = "SELECT SUM(amount) AS sum, SUM(amount_tax) TaxSum FROM invoice WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3" +
            " AND type = ?4 AND disable = ?5", nativeQuery = true)
    InvoiceSumAmountAndTaxDto getSumAmountByType(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, String type, int disable);

    @Query(value = "SELECT * FROM invoice WHERE car_license_num = ?1 AND " +
            "handle_date between ?2 AND ?3" +
            " AND TYPE = ?4", nativeQuery = true)
    Page<Invoice> getAllByType(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, String type, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE invoice SET handle_date = ?2, invoice_date = ?3, invoice_num = ?4, " +
            "amount = ?5, amount_tax = ?6, car_agency = ?7, car_agency_num = ?8, note = ?9, disable = ?10, tax_month = ?11, last_modify_time = ?12 " +
            "WHERE id = ?1",
            nativeQuery = true)
    int updateInvoice(int id,
                      String handleDate,
                      String invoiceDate,
                      String invoiceNum,
                      BigDecimal invoiceAmount,
                      BigDecimal invoiceTax,
                      String carAgency,
                      int carAgencyNum,
                      String note,
                      int disable,
                      String taxMonth, long now);

    @Query(value = "SELECT * FROM invoice WHERE car_license_num = ?1 AND handle_date between ?2 AND ?3 AND disable = ?4"
            , nativeQuery = true)
    List<Invoice> getDetailByInvoiceDate(String carLicenseNum, LocalDate monthFirstDate, LocalDate monthLastDate, int disable);

}
