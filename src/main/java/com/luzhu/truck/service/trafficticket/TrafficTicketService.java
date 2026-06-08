package com.luzhu.truck.service.trafficticket;

import com.luzhu.truck.dao.trafficticket.TrafficTicketDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.trafficticket.AddTrafficTicketParam;
import com.luzhu.truck.dto.trafficticket.UpdateTrafficTicketParam;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.util.OtherLendMoneyTypeUtil;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class TrafficTicketService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private TrafficTicketDao trafficTicketDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addTrafficTicket(AddTrafficTicketParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = trafficTicketDao.insertTicket(param.getCarLicenseNum(), param.getHandleDate(), param.getTicketDate(), param.getGoPoliceDate(), param.getPayDate(), param.getTicketNum(),
                param.getAmount(), OtherLendMoneyTypeUtil.NORMAL, null, null, param.getDisable(), param.getNote(), l , l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForHandleMonths("罰單新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getHandleDate()));
    }

    @Transactional
    public void updateTrafficTicket(UpdateTrafficTicketParam param) {
        TrafficTicket before = trafficTicketDao.findById(param.getId().intValue())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.TRAFFIC_TICKET_DISABLE));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(before.getType()),
                new AppException(SystemExceptionEnum.TRAFFIC_TICKET_ADJUSTMENT_NO_REBILL));

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = trafficTicketDao.updateTicket(param.getCarLicenseNum(), param.getHandleDate(), param.getTicketDate(), param.getGoPoliceDate(), param.getPayDate(), param.getTicketNum(),
                param.getAmount(), param.getDisable(), param.getNote(), l , param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForHandleMonths("罰單更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getHandleDate()),
                DateTimeUtil.toBillYearMonth(param.getHandleDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增 ADJUSTMENT 列於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        TrafficTicket src = trafficTicketDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.TRAFFIC_TICKET_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetTrafficTicketId() == null,
                new AppException(SystemExceptionEnum.TRAFFIC_TICKET_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourceTrafficTicketId() == null,
                new AppException(SystemExceptionEnum.TRAFFIC_TICKET_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(src.getType()),
                new AppException(SystemExceptionEnum.TRAFFIC_TICKET_ADJUSTMENT_NO_REBILL));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        String srcBillYearMonth = DateTimeUtil.toBillYearMonth(src.getHandleDate());

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        // 目標月份與來源月份相同：僅作廢原列，不新增調整列（否則兩筆相抵等於未報廢）
        if (param.getTargetBillYearMonth().equals(srcBillYearMonth)) {
            src.setDisable(1);
            src.setLastModifyTime(l);
            if (param.getNote() != null && !param.getNote().isBlank()) {
                src.setNote(param.getNote());
            }
            trafficTicketDao.save(src);
            refreshMonthBillSnapshotsForHandleMonths("罰單報廢", src.getCarLicenseNum(), srcBillYearMonth);
            return;
        }

        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newHandleDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        TrafficTicket neu = new TrafficTicket();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourceTrafficTicketId", "rebillTargetTrafficTicketId");
        neu.setType(OtherLendMoneyTypeUtil.toAdjustmentType());
        neu.setHandleDate(newHandleDate);
        neu.setDisable(0);
        neu.setRebillSourceTrafficTicketId(src.getId());
        neu.setRebillTargetTrafficTicketId(null);
        neu.setCreateTime(l);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = trafficTicketDao.save(neu);

        int marked = trafficTicketDao.markOriginalTrafficTicketRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForHandleMonths("罰單報廢轉月", src.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(src.getHandleDate()),
                param.getTargetBillYearMonth());
    }

    private void refreshMonthBillSnapshotsForHandleMonths(String historyRemark, String carLicenseNum, String... yearMonths) {
        if (carLicenseNum == null || carLicenseNum.isBlank()) {
            return;
        }
        Set<String> months = new LinkedHashSet<>();
        for (String ym : yearMonths) {
            if (ym != null && !ym.isBlank()) {
                months.add(ym);
            }
        }
        if (months.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        for (String billDate : months) {
            DateTimeValidate.checkYearMonth(billDate);
            MonthBillReq req = new MonthBillReq();
            req.setCarLicenseNum(carLicenseNum);
            req.setBillDate(billDate);
            MonthBillResponse monthBill = billService.getMonthBillForceRecalculate(req);
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "TRAFFIC_TICKET", now, historyRemark);
        }
    }

    public PageResult<TrafficTicket> getTrafficTicketList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<TrafficTicket> list = trafficTicketDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
