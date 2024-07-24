package com.luzhu.truck.controller.car;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarCache carCache;
    @PostMapping("/carInfo")
    public ResponseModel<List<CarInfo>> getCarInfo() throws ExecutionException {
        List<CarInfo> all = carCache.getAllCars("all");
        return new ResponseModel<>(all);
    }
}
