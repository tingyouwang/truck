package com.luzhu.truck.service.receiveoffset;

import com.luzhu.truck.dao.receiveoffset.ReceiveOffsetDao;
import com.luzhu.truck.dto.receiveoffset.AddReceiveOffsetParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ReceiveOffsetService {
    @Autowired
    private ReceiveOffsetDao receiveOffsetDao;
    @Transactional
    public void addReceiveOffset(AddReceiveOffsetParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = receiveOffsetDao.insertReceiveOffset(param.getCarLicenseNum(), param.getPayDate(), param.getAmount(),
                param.getReceiptAmount(), param.getNote(), l , l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

}
