package com.luzhu.truck.service.invoice;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * addInvoice 補開立月份可能早於目前月份（例如六月初拿五月發票來請款），
 * 此時除了發票所屬的入帳月份外，也要連同更新「現在」這個月的帳單快照。
 */
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceDao invoiceDao;
    @Mock
    private CarFeeDao carFeeDao;
    @Mock
    private BillService billService;
    @Mock
    private MonthBillSnapshotService monthBillSnapshotService;

    private InvoiceService invoiceService;

    private static final String TIME_OFFSET = "8";

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService();
        ReflectionTestUtils.setField(invoiceService, "invoiceDao", invoiceDao);
        ReflectionTestUtils.setField(invoiceService, "carFeeDao", carFeeDao);
        ReflectionTestUtils.setField(invoiceService, "billService", billService);
        ReflectionTestUtils.setField(invoiceService, "monthBillSnapshotService", monthBillSnapshotService);
        ReflectionTestUtils.setField(invoiceService, "timeOffset", TIME_OFFSET);
    }

    private AddInvoiceParam newParam(String handleDate) {
        AddInvoiceParam param = new AddInvoiceParam();
        param.setType("SALE");
        param.setCarLicenseNum("ABC-1234");
        param.setHandleDate(handleDate);
        param.setInvoiceDate(handleDate);
        param.setAmount(BigDecimal.valueOf(1000));
        param.setCarAgency("車行A");
        return param;
    }

    @Test
    void addInvoice_backdatedToPreviousMonth_refreshesBothInvoiceMonthAndCurrentMonth() {
        when(invoiceDao.insertInvoice(any(), any(), any(), any(), any(), any(), any(Integer.class),
                any(Integer.class), any(), any(), any(), any(), any(), any(), any(Long.class)))
                .thenReturn(1);
        when(carFeeDao.getCarFeeByLicenseNum(anyString())).thenReturn(new CarFee());
        when(billService.getMonthBillForceRecalculate(any(MonthBillReq.class))).thenReturn(new MonthBillResponse());

        String currentBillYearMonth = YearMonth.now(ZoneOffset.ofHours(Integer.parseInt(TIME_OFFSET)))
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String invoiceBillYearMonth = YearMonth.parse(currentBillYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"))
                .minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String handleDate = invoiceBillYearMonth + "-28";

        invoiceService.addInvoice(newParam(handleDate));

        ArgumentCaptor<MonthBillReq> reqCaptor = ArgumentCaptor.forClass(MonthBillReq.class);
        verify(billService, times(2)).getMonthBillForceRecalculate(reqCaptor.capture());
        List<String> billDates = reqCaptor.getAllValues().stream().map(MonthBillReq::getBillDate).toList();
        assertTrue(billDates.contains(invoiceBillYearMonth));
        assertTrue(billDates.contains(currentBillYearMonth));

        verify(monthBillSnapshotService, times(2)).saveSnapshot(
                anyString(), anyString(), any(MonthBillResponse.class), anyString(), any(), anyString());
    }

    @Test
    void addInvoice_handleDateInCurrentMonth_onlyRefreshesOnce() {
        when(invoiceDao.insertInvoice(any(), any(), any(), any(), any(), any(), any(Integer.class),
                any(Integer.class), any(), any(), any(), any(), any(), any(), any(Long.class)))
                .thenReturn(1);
        when(carFeeDao.getCarFeeByLicenseNum(anyString())).thenReturn(new CarFee());
        when(billService.getMonthBillForceRecalculate(any(MonthBillReq.class))).thenReturn(new MonthBillResponse());

        String currentBillYearMonth = YearMonth.now(ZoneOffset.ofHours(Integer.parseInt(TIME_OFFSET)))
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String handleDate = currentBillYearMonth + "-05";

        invoiceService.addInvoice(newParam(handleDate));

        ArgumentCaptor<MonthBillReq> reqCaptor = ArgumentCaptor.forClass(MonthBillReq.class);
        verify(billService, times(1)).getMonthBillForceRecalculate(reqCaptor.capture());
        assertEquals(currentBillYearMonth, reqCaptor.getValue().getBillDate());

        verify(monthBillSnapshotService, times(1)).saveSnapshot(
                anyString(), anyString(), any(MonthBillResponse.class), anyString(), any(), anyString());
    }
}
