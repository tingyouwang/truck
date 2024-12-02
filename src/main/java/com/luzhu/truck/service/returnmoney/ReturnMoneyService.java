package com.luzhu.truck.service.returnmoney;

import com.luzhu.truck.dao.returnmoney.ReturnMoneyDao;
import com.luzhu.truck.dto.returnmoney.AddReturnMoneyParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ReturnMoneyService {
    @Autowired
    private ReturnMoneyDao returnMoneyDao;
    @Transactional
    public void addReturnMoney(AddReturnMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = returnMoneyDao.insertReturnMoney(param.getCarLicenseNum(), param.getPayDate(), param.getAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

}
