package com.luzhu.truck.dao.invoice;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.invoice.Invoice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InvoiceDao extends BaseDao<Invoice, Integer> {
    @Modifying
    @Query(value = "INSERT INTO invoice (invoice_num, invoice_date, " +
            "handle_date, amount, amount_tax, car_agency, car_agency_num, disable, note, tax_month, car_license_num) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11)", nativeQuery = true)
    int insertInvoice(String invoiceNum, String invoiceDate, String handleDate, BigDecimal amount, BigDecimal amountTax,
                      String carAgency, int carAgencyNum, int disable, String note, String taxMonth, String carLicenseNum);

}
