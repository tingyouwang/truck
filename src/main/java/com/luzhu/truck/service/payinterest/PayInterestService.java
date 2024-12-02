package com.luzhu.truck.service.payinterest;

import com.luzhu.truck.dao.payinterest.PayInterestDao;
import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.dto.payinterest.AddPayInterestParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class PayInterestService {
    @Autowired
    private PayInterestDao payInterestDao;

    @Transactional
    public void addPayInterest(AddPayInterestParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = payInterestDao.insertPayInterest(param.getCarLicenseNum(), param.getPayDate(),
                param.getAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
