package com.luzhu.truck.service.car;


import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.owner.OwnerDao;
import com.luzhu.truck.dto.car.*;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.owner.Owner;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    public List<CarInfo> getAllCarForDropDown() {
        return carDao.getAllCarForDropDown();
    }

    public PageResult<CarInfo> searchCarByLicenseNum(SearchCarLicenseNumParam param) {
        if (StringUtils.hasText(param.getLicenseNumber())) {
            return new PageResult<>(carDao.searchCarByLicenseNum(param.getLicenseNumber(), param.getPageable()));
        } else {
            return new PageResult<>(carDao.getAllCar(param.getPageable()));
        }
    }



    @Transactional
    public void addCar(AddCarParam addCarParam) {
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
    public void updateCar(UpdateCarParam param) {
        int updateCount = carDao.updateCar(
                param.getLicenseNumber(),
                param.getIsUsing(),
                param.getOwnerName(),
                param.getCarAgency(),
                param.getJoinDate(), // 遷入日期
                param.getQuitDate(), // 遷出日期
                param.getJoinAmount(),
                param.getQuitAmount(),
                param.getCarFrom(),
                param.getQuitPlace(),
                param.getLicenseIssueDate(), // 發照日期
                param.getManufactureDate(), // 出廠日期
                param.getBrand(),
                param.getTon(),
                Double.parseDouble(param.getCc()), // cc 數字轉換
                param.getEngineNum(),
                param.getInspectionDate(), // 驗車日期
                param.getRenewLicenseDate(), // 換照日期
                param.getCarTypeOutlooking(),
                param.getPassLicense(),
                param.getCarWeight(),
                param.getLoadingWeight(),
                param.getCarType(),
                param.getInspectionType(),
                param.getViolationDate(), // 超載到期
                param.getReportStopDate(), // 報停日期
                param.getReportScrapDate(), // 報銷日期
                param.getOldLicenseNumber(),
                param.getNote1(),
                param.getNote2(),
                param.getId() // ID
        );
        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void addCarOwner(AddCarOwnerParam addCarOwnerParam) {
        int i = ownerDao.addOwner(
                addCarOwnerParam.getName(),
                addCarOwnerParam.getIdNum(),
                addCarOwnerParam.getSex(),
                addCarOwnerParam.getBirthday(),
                addCarOwnerParam.getPhone1(),
                addCarOwnerParam.getPhone2(),
                addCarOwnerParam.getMobile(),
                addCarOwnerParam.getFax(),
                addCarOwnerParam.getAddress(),
                addCarOwnerParam.getMailAddress()
        );
        Validator.isFalseThrow(1 == i,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateCarOwner(UpdateCarOwnerParam updateCarOwnerParam) {
        int i = ownerDao.updateOwner(
                updateCarOwnerParam.getName(),
                updateCarOwnerParam.getPhone1(),
                updateCarOwnerParam.getIdNum(),
                updateCarOwnerParam.getSex(),
                updateCarOwnerParam.getBirthday(),
                updateCarOwnerParam.getPhone2(),
                updateCarOwnerParam.getMobile(),
                updateCarOwnerParam.getFax(),
                updateCarOwnerParam.getAddress(),
                updateCarOwnerParam.getMailAddress(),
                updateCarOwnerParam.getId()
        );
        Validator.isFalseThrow(1 == i,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    public PageResult<Owner> getCarOwner(SearchCarOwnerParam param) {

        if (StringUtils.hasText(param.getSearchName())) {
            return new PageResult<>(ownerDao.searchByName(param.getSearchName(), param.getPageable()));
        } else {
            return new PageResult<>(ownerDao.getAllOwner(param.getPageable()));
        }
    }

    public List<CarOwnerDropDownDto> getCarOwnerDropDown() {
        return ownerDao.getAllOwnerDropDown();
    }

    public Owner getCarOwnerById(long id) {
        Owner owner = ownerDao.getOwnerById(id);
        return owner;
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

    public CarFee getCarFeeByLicenseNum(String licenseNum) {
        return carFeeDao.getCarFeeByLicenseNum(licenseNum);
    }
}
