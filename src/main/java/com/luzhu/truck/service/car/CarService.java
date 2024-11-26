package com.luzhu.truck.service.car;

import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.owner.OwnerDao;
import com.luzhu.truck.dto.car.AddCarFeeParam;
import com.luzhu.truck.dto.car.AddCarParam;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarDao carDao;
    @Autowired
    private OwnerDao ownerDao;
    @Autowired
    private CarFeeDao carFeeDao;
    @Value("${env.time.offset}")
    private String timeOffset;

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

    @Transactional
    public void addCarFee(AddCarFeeParam addCarParam) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));

        int insertCount = carFeeDao.addCarFee(addCarParam.getCarLicenseNum(), addCarParam.getManageFee(), addCarParam.getSaleTax(), addCarParam.getBuyTax(),
                addCarParam.getGasTax(), addCarParam.getOweTax(), addCarParam.getReceipTax(),
                addCarParam.getFuelTaxSpring(), addCarParam.getFuelTaxSummer(), addCarParam.getFuelTaxAutumn(), addCarParam.getFuelTaxWinter(),
                addCarParam.getLicenseTaxFirstHalf(), addCarParam.getLicenseTaxSecondHalf(), addCarParam.getUnionFee(), addCarParam.getLaborFee(),
                addCarParam.getHealthyFee(), addCarParam.getReadyFee(), addCarParam.getPeopleHelpFee(), now,
                now, "aaadmin");

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
