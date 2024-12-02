package com.luzhu.truck.service.trafficticket;

import com.luzhu.truck.dao.trafficticket.TrafficTicketDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.trafficticket.AddTrafficTicketParam;
import com.luzhu.truck.dto.trafficticket.UpdateTrafficTicketParam;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class TrafficTicketService {
    @Autowired
    private TrafficTicketDao trafficTicketDao;
    @Transactional
    public void addTicket(AddTrafficTicketParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = trafficTicketDao.insertTicket(param.getCarLicenseNum(), param.getHandleDate(), param.getTicketDate(), param.getGoPoliceDate(), param.getPayDate(), param.getTicketNum(),
                param.getAmount(), param.getNote(), l , l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateTicket(UpdateTrafficTicketParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = trafficTicketDao.updateTicket(param.getCarLicenseNum(), param.getHandleDate(), param.getTicketDate(), param.getGoPoliceDate(), param.getPayDate(), param.getTicketNum(),
                param.getAmount(), param.getNote(), l , param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    public PageResult<TrafficTicket> getTicketList(LicenseAndExpenseYearMonthParam param) {
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
