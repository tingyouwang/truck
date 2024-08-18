package com.luzhu.truck.service.otherlendmoney;

import com.luzhu.truck.dao.otherlendmoney.OtherLendMoneyDao;
import com.luzhu.truck.dto.otherlendmoney.AddOtherLendMoneyParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class OtherLendMoneyService {
    @Autowired
    private OtherLendMoneyDao otherLendMoneyDao;
    @Transactional
    public void addOtherLendMoney(AddOtherLendMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = otherLendMoneyDao.insertOtherLendMoney(param.getCarLicenseNum(), param.getLendDate(), param.getAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
