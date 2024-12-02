package com.luzhu.truck.service.givebackmoney;

import com.luzhu.truck.dao.givebackmoney.GiveBackMoneyDao;
import com.luzhu.truck.dto.givebackmoney.AddGiveBackMoneyParam;
import com.luzhu.truck.dto.givebackmoney.UpdateGiveBackMoneyParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class GiveBackMoneyService {
    @Autowired
    private GiveBackMoneyDao giveBackMoneyDao;
    @Transactional
    public void addGiveBackMoney(AddGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = giveBackMoneyDao.insertLendMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getType(), param.getExpireDate(),
                param.getInterestAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateGiveBackMoney(UpdateGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = giveBackMoneyDao.updateGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getType(), param.getExpireDate(),
                param.getInterestAmount(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
