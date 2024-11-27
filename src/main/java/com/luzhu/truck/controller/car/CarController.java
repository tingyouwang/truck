package com.luzhu.truck.controller.car;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.IdParam;
import com.luzhu.truck.dto.LicenseNumParam;
import com.luzhu.truck.dto.car.*;
import com.luzhu.truck.entity.caragency.CarAgency;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.owner.Owner;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.car.CarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/car")
@CrossOrigin("*")
public class CarController {
    @Autowired
    private CarCache carCache;
    @Autowired
    private CarService carService;
    @PostMapping("/carInfoDropDownList")
    public ResponseModel<List<CarInfo>> getCarInfo() throws ExecutionException {
        List<CarInfo> all = carCache.getAllCars("all");
        return new ResponseModel<>(all);
    }

    @PostMapping("/carOwnerDropDownList")
    public ResponseModel<List<CarOwnerDropDownDto>> getCarOwnerDropDown() throws ExecutionException {
        List<CarOwnerDropDownDto> carOwnerDropDown = carService.getCarOwnerDropDown();
        return new ResponseModel<>(carOwnerDropDown);
    }

    @PostMapping("/searchCarByLicenseNum")
    public ResponseModel<PageResult<CarInfo>> getCarList(@RequestBody SearchCarLicenseNumParam param) throws ExecutionException {
        PageResult<CarInfo> carInfoPageResult = carService.searchCarByLicenseNum(param);
        return new ResponseModel<>(carInfoPageResult);
    }
    @PostMapping("/addCar")
    public ResponseModel<Object> addCarInfo(@RequestBody @Valid AddCarParam param) {
        carService.addCar(param);
        carCache.invalidate();
        return new ResponseModel<>();
    }

    @PostMapping("/updateCar")
    public ResponseModel<Object> updateCarInfo(@RequestBody @Valid UpdateCarParam param) {
        carService.updateCar(param);
        carCache.invalidate();
        return new ResponseModel<>();
    }

    @PostMapping("/addCarOwner")
    public ResponseModel<Object> addCarOwner(@RequestBody @Valid AddCarOwnerParam param) throws ExecutionException {
        carService.addCarOwner(param);
        return new ResponseModel<>();
    }

    @PostMapping("/updateCarOwner")
    public ResponseModel<Object> updateCarOwner(@RequestBody @Valid UpdateCarOwnerParam param) throws ExecutionException {
        carService.updateCarOwner(param);
        return new ResponseModel<>();
    }
    @PostMapping("/getCarOwner")
    public ResponseModel<PageResult<Owner>> getCarOwner(@RequestBody SearchCarOwnerParam param) {
        PageResult<Owner> carOwner = carService.getCarOwner(param);
        return new ResponseModel<>(carOwner);
    }

    @PostMapping("/getCarOwnerById")
    public ResponseModel<Owner> getCarOwnerByName(@RequestBody IdParam param) {
        Owner carOwner = carService.getCarOwnerById(param.getId());
        return new ResponseModel<>(carOwner);
    }

    @PostMapping("/addCarFee")
    public ResponseModel<Object> addCarFee(@RequestBody @Valid AddCarFeeParam param) throws ExecutionException {
        carService.addCarFee(param);
        return new ResponseModel<>();
    }

    @PostMapping("/getCarFee")
    public ResponseModel<CarFee> getCarFee(@RequestBody @Valid LicenseNumParam licenseNum) {
        return new ResponseModel<>(carService.getCarFeeByLicenseNum(licenseNum.getCarLicenseNum()));
    }
}
