package com.luzhu.truck.controller.car;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.IdParam;
import com.luzhu.truck.dto.car.*;
import com.luzhu.truck.entity.caragency.CarAgency;
import com.luzhu.truck.entity.owner.Owner;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.car.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/carInfo")
    public ResponseModel<List<CarInfo>> getCarInfo() throws ExecutionException {
        List<CarInfo> all = carCache.getAllCars("all");
        return new ResponseModel<>(all);
    }
    @PostMapping("/addCar")
    public ResponseModel<Object> addCarInfo(@RequestBody @Valid AddCarParam param) throws ExecutionException {
        carService.addCar(param);
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
}
