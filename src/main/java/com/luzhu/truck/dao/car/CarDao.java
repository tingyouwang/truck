package com.luzhu.truck.dao.car;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.entity.Car;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarDao extends BaseDao<Car, Integer> {
    @Query(value = "SELECT license_number FROM car WHERE is_using = 1",
    nativeQuery = true)
    List<String> getAllLicenseNumber();
    @Query(value = "SELECT id, license_number as licenseNumber, owner_name as ownerName FROM car WHERE is_using = 1",
            nativeQuery = true)
    List<CarInfo> getAllCar();


}
