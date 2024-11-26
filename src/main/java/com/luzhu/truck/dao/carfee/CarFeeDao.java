package com.luzhu.truck.dao.carfee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.carfee.CarFee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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

    @Modifying
    @Query(value = "INSERT INTO car_fee (car_license_num, manage_fee, sale_tax, buy_tax, gas_tax, owe_tax, receip_tax, fuel_tax_spring, fuel_tax_summer, fuel_tax_autumn, fuel_tax_winter, license_tax_first_half, license_tax_second_half, union_fee, labor_fee, healthy_fee, ready_fee, people_help_fee, create_time, update_time, update_by) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21)",
            nativeQuery = true)
    int addCarFee(String carLicenseNum, double manageFee, double saleTax, double buyTax, double gasTax, double oweTax, double receipTax,
                  double fuelTaxSpring, double fuelTaxSummer, double fuelTaxAutumn, double fuelTaxWinter, double licenseTaxFirstHalf,
                  double licenseTaxSecondHalf, double unionFee, double laborFee, double healthyFee, double readyFee, double peopleHelpFee,
                  LocalDateTime createTime, LocalDateTime updateTime, String updateBy);


}
