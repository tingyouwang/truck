package com.luzhu.truck.service.trafficticket;

import com.luzhu.truck.dao.trafficticket.TrafficTicketDao;
import com.luzhu.truck.dto.trafficticket.AddTrafficTicketParam;
import com.luzhu.truck.dto.trafficticket.UpdateTrafficTicketParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
}
