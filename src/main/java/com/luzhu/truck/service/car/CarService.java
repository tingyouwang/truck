package com.luzhu.truck.service.car;


import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.owner.OwnerDao;
import com.luzhu.truck.dto.car.*;
import com.luzhu.truck.entity.Car;
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
import java.time.format.DateTimeFormatter;
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

    public PageResult<CarInfo> searchCarByOwner(SearchCarOwnerParam param) {
        return new PageResult<>(carDao.searchCarByOwner(param.getSearchName(), param.getPageable()));
    }

    public Car getCarByLicense(SearchCarLicenseNumParam param) {
        return carDao.getCarByLicenseNum(param.getLicenseNumber());
    }



    @Transactional
    public void addCar(AddCarParam addCarParam) {
        int insertCount = carDao.addCar(
                addCarParam.getLicenseNumber(),
                1, // Assuming `isUsing` is always 1 for this example
                addCarParam.getOwnerName(),
                addCarParam.getCarAgencyId(),
                addCarParam.getJoinDate(),
                addCarParam.getQuitDate(),
                addCarParam.getJoinAmount(),
                addCarParam.getQuitAmount(),
                addCarParam.getCarFrom(),
                addCarParam.getQuitPlace(),
                addCarParam.getLicenseIssueDate(),
                addCarParam.getManufactureYearMonth(),
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
                addCarParam.getNote2(),
                addCarParam.getCarAgency()
        );
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateCar(UpdateCarParam param) {
        String violateDate = StringUtils.hasText(param.getViolationDate()) ? param.getViolationDate() : null;
        String reportStopDate = StringUtils.hasText(param.getReportStopDate()) ? param.getReportStopDate() : null;
        String reportScrapDate = StringUtils.hasText(param.getReportScrapDate()) ? param.getReportScrapDate() : null;
        int updateCount = carDao.updateCar(
                param.getIsUsing(),
                param.getOwnerName(),
                param.getCarAgencyId(),
                param.getJoinDate(), // 遷入日期
                param.getQuitDate(), // 遷出日期
                param.getJoinAmount(),
                param.getQuitAmount(),
                param.getCarFrom(),
                param.getQuitPlace(),
                param.getLicenseIssueDate(), // 發照日期
                param.getManufactureYearMonth(), // 出廠日期
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
                violateDate, // 超載到期
                reportStopDate, // 報停日期
                reportScrapDate, // 報銷日期
                param.getOldLicenseNumber(),
                param.getNote1(),
                param.getNote2(),
                param.getId(), // ID
                param.getCarAgency()
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
    public void addCarFee(AddCarFeeParam param) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));

        CarFee carFee = new CarFee();
        carFee.setCarLicenseNum(param.getCarLicenseNum());
        carFee.setManageFee(param.getManageFee());
        carFee.setSaleTax(param.getSaleTax());
        carFee.setBuyTax(param.getBuyTax());
        carFee.setGasTax(param.getGasTax());
        carFee.setOweTax(param.getOweTax());
        carFee.setGiveBackTax(param.getGiveBackTax());
        carFee.setReceipTax(param.getReceipTax());
        carFee.setFuelTaxSpring(param.getFuelTaxSpring());
        carFee.setFuelTaxSummer(param.getFuelTaxSummer());
        carFee.setFuelTaxAutumn(param.getFuelTaxAutumn());
        carFee.setFuelTaxWinter(param.getFuelTaxWinter());
        carFee.setLicenseTaxFirstHalf(param.getLicenseTaxFirstHalf());
        carFee.setLicenseTaxSecondHalf(param.getLicenseTaxSecondHalf());
        carFee.setUnionFee(param.getUnionFee());
        carFee.setLaborFee(param.getLaborFee());
        carFee.setHealthyFee(param.getHealthyFee());
        carFee.setReadyFee(param.getReadyFee());
        carFee.setPeopleHelpFee(param.getPeopleHelpFee());
        carFee.setCreateTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // 設定創建時間
        carFee.setUpdateTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // 設定更新時間
        carFee.setUpdateBy("system"); // 設定更新者，實際可改為登入用戶
        CarFee save = carFeeDao.save(carFee);

//        int insertCount = carFeeDao.addCarFee(addCarParam.getCarLicenseNum(), addCarParam.getManageFee(), addCarParam.getSaleTax(), addCarParam.getBuyTax(),
//                addCarParam.getGasTax(), addCarParam.getOweTax(), addCarParam.getReceipTax(),
//                addCarParam.getFuelTaxSpring(), addCarParam.getFuelTaxSummer(), addCarParam.getFuelTaxAutumn(), addCarParam.getFuelTaxWinter(),
//                addCarParam.getLicenseTaxFirstHalf(), addCarParam.getLicenseTaxSecondHalf(), addCarParam.getUnionFee(), addCarParam.getLaborFee(),
//                addCarParam.getHealthyFee(), addCarParam.getReadyFee(), addCarParam.getPeopleHelpFee(), now,
//                now, "aaadmin");
//
//        Validator.isFalseThrow(1 == insertCount,
//                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    public CarFee getCarFeeByLicenseNum(String licenseNum) {
        return carFeeDao.getCarFeeByLicenseNum(licenseNum);
    }

    @Transactional
    public void updateOwnerStatus(UpdateOwnerStatusParam param) {
        int updateCount = ownerDao.updateOwnerStatus(param.getId(), param.getStatus());
        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
