package com.luzhu.truck.service.car;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.entity.Car;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarDao carDao;
    public List<CarInfo> getAllCar() {
        return carDao.getAllCar();
    }
}
