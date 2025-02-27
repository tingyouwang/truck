package com.luzhu.truck.service.caragency;

import com.luzhu.truck.dao.caragency.CarAgencyDao;
import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.caragency.AddCarAgencyParam;
import com.luzhu.truck.dto.caragency.CarAgencyDropDownDTO;
import com.luzhu.truck.dto.caragency.UpdateCarAgencyParam;
import com.luzhu.truck.entity.caragency.CarAgency;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CarAgencyService {
    @Autowired
    private CarAgencyDao carAgencyDao;
    public PageResult<CarAgency> getCarAgency(BaseParam param) {
//        return carAgencyDao.getAllCarAgency(param.getPageable());
        Page<CarAgency> allCarAgency = carAgencyDao.getAllCarAgency(param.getPageable());
        return new PageResult<>(allCarAgency);
    }

    public List<CarAgencyDropDownDTO> dropDownGetCarAgency() {
        return carAgencyDao.dropDownGetCarAgency();
    }

    @Transactional
    public void addCarAgency(AddCarAgencyParam addCarAgencyParam) {
        int i = carAgencyDao.countByAgencyName(addCarAgencyParam.getAgencyName());
        Validator.isFalseThrow(0 == i,
                new AppException(SystemExceptionEnum.CAR_AGENCY_NAME_DUPLICATE));

        int insertCount = carAgencyDao.insertCarAgency(addCarAgencyParam.getAgencyName(), addCarAgencyParam.getAddress(), addCarAgencyParam.getOwner(), addCarAgencyParam.getTaxId(),
                addCarAgencyParam.getPhone1(), addCarAgencyParam.getPhone2(), addCarAgencyParam.getMobile(), addCarAgencyParam.getFax());
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateCarAgency(UpdateCarAgencyParam updateCarAgencyParam) {
        int i = carAgencyDao.countByAgencyName(updateCarAgencyParam.getAgencyName(), updateCarAgencyParam.getId());
        Validator.isFalseThrow(0 == i,
                new AppException(SystemExceptionEnum.CAR_AGENCY_NAME_DUPLICATE));
        CarAgency carAgency = new CarAgency();
        carAgency.setId(updateCarAgencyParam.getId());
        carAgency.setAgencyName(updateCarAgencyParam.getAgencyName());
        carAgency.setAddress(updateCarAgencyParam.getAddress());
        carAgency.setOwner(updateCarAgencyParam.getOwner());
        carAgency.setTaxId(updateCarAgencyParam.getTaxId());
        carAgency.setPhone1(updateCarAgencyParam.getPhone1());
        carAgency.setPhone2(updateCarAgencyParam.getPhone2());
        carAgency.setMobile(updateCarAgencyParam.getMobile());
        carAgency.setFax(updateCarAgencyParam.getFax());

        carAgencyDao.save(carAgency);
    }

    @Transactional
    public void deleteCarAgency(int id) {
        int deleteCount = carAgencyDao.deleteCarAgencyById(id);
        Validator.isFalseThrow(1 == deleteCount,
                new AppException(SystemExceptionEnum.DELETE_ERROR));
    }

//    public static void main(String[] args) {
//        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy/MM/dd")
//                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);
//
//        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy/MM/dd")
//                .withChronology()
//    }


}
