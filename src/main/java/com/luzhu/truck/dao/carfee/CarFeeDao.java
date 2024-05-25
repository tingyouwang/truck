package com.luzhu.truck.dao.carfee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.carfee.CarFee;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarFeeDao extends BaseDao<CarFee, String> {
    @Query(value = "SELECT * FROM car_fee WHERE car_license_num IN (SELECT license_number FROM car WHERE is_using = 1)",
    nativeQuery = true)
    List<CarFee> getUsingCarFee();

    @Query(value = "SELECT * FROM car_fee WHERE car_license_num IN (SELECT license_number FROM car WHERE is_using = 1)" +
            "AND manage_fee > 0",
            nativeQuery = true)
    List<CarFee> getUsingCarFeeForManageFee();
    @Query(value = "SELECT * FROM car_fee WHERE car_license_num IN (SELECT license_number FROM car WHERE is_using = 1)" +
            "AND union_fee > 0",
            nativeQuery = true)
    List<CarFee> getUsingCarFeeForUnionFee();


}
