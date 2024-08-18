package com.luzhu.truck.service.othergivebackmoney;

import com.luzhu.truck.dao.othergivebackmoney.OtherGiveBackMoneyDao;
import com.luzhu.truck.dto.othergivebackmoney.AddOtherGiveBackMoneyParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class OtherGiveBackMoneyService {
    @Autowired
    private OtherGiveBackMoneyDao otherGiveBackMoneyDao;
    @Transactional
    public void addOtherGiveBackMoney(AddOtherGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = otherGiveBackMoneyDao.insertOtherGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
