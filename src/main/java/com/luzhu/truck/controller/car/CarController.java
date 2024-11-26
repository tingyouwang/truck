package com.luzhu.truck.controller.car;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.car.AddCarFeeParam;
import com.luzhu.truck.dto.car.AddCarParam;
import com.luzhu.truck.dto.car.CarInfo;
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

    @PostMapping("/addCarFee")
    public ResponseModel<Object> addCarFee(@RequestBody @Valid AddCarFeeParam param) throws ExecutionException {
        carService.addCarFee(param);
        return new ResponseModel<>();
    }
}
