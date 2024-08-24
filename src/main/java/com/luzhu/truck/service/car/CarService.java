package com.luzhu.truck.service.car;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.owner.OwnerDao;
import com.luzhu.truck.dto.car.AddCarParam;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.entity.Car;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarDao carDao;
    @Autowired
    private OwnerDao ownerDao;

    public List<CarInfo> getAllCar() {
        return carDao.getAllCar();
    }

    @Transactional
    public void addCar(AddCarParam addCarParam) {
        int i = ownerDao.addOwner(
                addCarParam.getName(),
                addCarParam.getIdNum(),
                addCarParam.getSex(),
                addCarParam.getBirthday(),
                addCarParam.getPhone1(),
                addCarParam.getPhone2(),
                addCarParam.getMobile(),
                addCarParam.getFax(),
                addCarParam.getAddress(),
                addCarParam.getMailAddress()
        );
        Validator.isFalseThrow(1 == i,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        int insertCount = carDao.addCar(
                addCarParam.getLicenseNumber(),
                1, // Assuming `isUsing` is always 1 for this example
                addCarParam.getOwnerName(),
                addCarParam.getCarAgency(),
                addCarParam.getJoinDate(),
                addCarParam.getQuitDate(),
                addCarParam.getJoinAmount(),
                addCarParam.getQuitAmount(),
                addCarParam.getCarFrom(),
                addCarParam.getQuitPlace(),
                addCarParam.getLicenseIssueDate(),
                addCarParam.getManufactureDate(),
                addCarParam.getBrand(),
                addCarParam.getTon(),
                Double.parseDouble(addCarParam.getCc()),  // Assuming cc is numeric
                addCarParam.getEngineNum(),
                addCarParam.getInspectionDate(),
                addCarParam.getRenewLicenseDate(),
                addCarParam.getCarTypeOutlooking(),
                addCarParam.getPassLicense(),
                addCarParam.getCarWeight(),
                addCarParam.getLoadingWeight(),
                addCarParam.getCarType(),
                addCarParam.getInspectionType(),
                addCarParam.getViolationDate(),
                addCarParam.getReportStopDate(),
                addCarParam.getReportScrapDate(),
                addCarParam.getOldLicenseNumber(),
                addCarParam.getNote1(),
                addCarParam.getNote2()
        );
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
