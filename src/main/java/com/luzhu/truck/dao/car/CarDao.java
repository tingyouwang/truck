package com.luzhu.truck.dao.car;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarDao extends BaseDao<Car, Integer> {
    @Query(value = "SELECT license_number FROM car WHERE is_using = 1",
    nativeQuery = true)
    List<String> getAllLicenseNumber();
    @Query(value = "SELECT id, license_number as licenseNumber, owner_name as ownerName FROM car WHERE is_using = 1",
            nativeQuery = true)
    List<CarInfo> getAllCarForDropDown();
    @Query(value = "SELECT * FROM car WHERE id = ?1",
    nativeQuery = true)
    Car getCarById(long id);

    @Modifying
    @Query(value = "INSERT INTO car (license_number, is_using, owner_name, car_agency, join_date, quit_date, join_amount, quit_amount, car_from, quit_place, license_issue_date, manufacture_year_month, brand, ton, cc, engine_num, inspection_date, renew_license_date, car_type_outlooking, pass_license, car_weight, loading_weight, car_type, inspection_type, violation_date, report_stop_date, report_scrap_date, old_license_number, note1, note2) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23, ?24, ?25, ?26, ?27, ?28, ?29, ?30)",
            nativeQuery = true)
    int addCar(String licenseNumber, int isUsing, String ownerName, String carAgency, String joinDate, String quitDate, Double joinAmount, Double quitAmount, String carFrom, String quitPlace, String licenseIssueDate, String manufactureDate, String brand, String ton, Double cc, String engineNum, String inspectionDate, String renewLicenseDate, String carTypeOutlooking, String passLicense, String carWeight, String loadingWeight, String carType, Double inspectionType, String violationDate, String reportStopDate, String reportScrapDate, String oldLicenseNumber, String note1, String note2);

    @Modifying
    @Query(value = "UPDATE car SET license_number = ?1, is_using = ?2, owner_name = ?3, car_agency = ?4, join_date = ?5, quit_date = ?6, join_amount = ?7, quit_amount = ?8, car_from = ?9, quit_place = ?10, license_issue_date = ?11, manufacture_year_month = ?12, brand = ?13, ton = ?14, cc = ?15, engine_num = ?16, inspection_date = ?17, renew_license_date = ?18, car_type_outlooking = ?19, pass_license = ?20, car_weight = ?21, loading_weight = ?22, car_type = ?23, inspection_type = ?24, violation_date = ?25, report_stop_date = ?26, report_scrap_date = ?27, old_license_number = ?28, note1 = ?29, note2 = ?30 WHERE id = ?31", nativeQuery = true)
    int updateCar(String licenseNumber, int isUsing, String ownerName, String carAgency, String joinDate, String quitDate, Double joinAmount, Double quitAmount, String carFrom, String quitPlace, String licenseIssueDate, String manufactureDate, String brand, String ton, Double cc, String engineNum, String inspectionDate, String renewLicenseDate, String carTypeOutlooking, String passLicense, String carWeight, String loadingWeight, String carType, Double inspectionType, String violationDate, String reportStopDate, String reportScrapDate, String oldLicenseNumber, String note1, String note2, long id);

    @Query(value = "SELECT * FROM car WHERE is_using = 1",
            countQuery = "SELECT * FROM car WHERE is_using = 1",
            nativeQuery = true)
    Page<CarInfo> getAllCar(Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND license_number LIKE %?1%",
            countQuery = "SELECT * FROM car WHERE is_using = 1 AND license_number LIKE %?1%",
            nativeQuery = true)
    Page<CarInfo> searchCarByLicenseNum(String num, Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND owner_name = ?1",
            countQuery = "SELECT * FROM car WHERE is_using = 1 AND owner_name = ?1",
            nativeQuery = true)
    Page<CarInfo> searchCarByOwner(String owner, Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND license_number = ?1",
            nativeQuery = true)
    Car getCarByLicenseNum(String licenseNum);


}
