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
    @Query(value = "SELECT license_number FROM car WHERE is_using = 1 AND status = 'enable'",
    nativeQuery = true)
    List<String> getAllLicenseNumber();
    @Query(value = "SELECT id, license_number as licenseNumber, owner_name as ownerName, car_agency as carAgency FROM car WHERE is_using = 1 AND status = 'enable'",
            nativeQuery = true)
    List<CarInfo> getAllCarForDropDown();
    @Query(value = "SELECT * FROM car WHERE id = ?1",
    nativeQuery = true)
    Car getCarById(long id);

    @Modifying
    @Query(value = "INSERT INTO car (license_number, is_using, owner_name, car_agency_id, join_date, quit_date, join_amount, quit_amount, car_from, quit_place, license_issue_date, manufacture_year_month, brand, ton, cc, engine_num, inspection_date, renew_license_date, car_type_outlooking, pass_license, car_weight, loading_weight, car_type, inspection_type, violation_date, report_stop_date, report_scrap_date, old_license_number, note1, note2, car_agency) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23, ?24, ?25, ?26, ?27, ?28, ?29, ?30, ?31)",
            nativeQuery = true)
    int addCar(String licenseNumber, int isUsing, String ownerName, int carAgencyId, String joinDate, String quitDate, Double joinAmount, Double quitAmount, String carFrom, String quitPlace, String licenseIssueDate, String manufactureDate, String brand, String ton, Double cc, String engineNum, String inspectionDate, String renewLicenseDate, String carTypeOutlooking, String passLicense, String carWeight, String loadingWeight, String carType, Double inspectionType, String violationDate, String reportStopDate, String reportScrapDate, String oldLicenseNumber, String note1, String note2, String carAgency);

    @Modifying
    @Query(value = "UPDATE car SET is_using = ?1, owner_name = ?2, car_agency_id = ?3, join_date = ?4, quit_date = ?5, join_amount = ?6, quit_amount = ?7, car_from = ?8, quit_place = ?9, license_issue_date = ?10, manufacture_year_month = ?11, brand = ?12, ton = ?13, cc = ?14, engine_num = ?15, inspection_date = ?16, renew_license_date = ?17, car_type_outlooking = ?18, pass_license = ?19, car_weight = ?20, loading_weight = ?21, car_type = ?22, inspection_type = ?23, violation_date = ?24, report_stop_date = ?25, report_scrap_date = ?26, old_license_number = ?27, note1 = ?28, note2 = ?29, car_agency = ?31 WHERE id = ?30",
            nativeQuery = true)
    int updateCar(int isUsing, String ownerName, int carAgencyId, String joinDate, String quitDate, Double joinAmount, Double quitAmount, String carFrom, String quitPlace, String licenseIssueDate, String manufactureDate, String brand, String ton, Double cc, String engineNum, String inspectionDate, String renewLicenseDate, String carTypeOutlooking, String passLicense, String carWeight, String loadingWeight, String carType, Double inspectionType, String violationDate, String reportStopDate, String reportScrapDate, String oldLicenseNumber, String note1, String note2, long id, String carAgency);


    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND status = 'enable'",
            countQuery = "SELECT COUNT(1) FROM car WHERE is_using = 1 AND status = 'enable'",
            nativeQuery = true)
    Page<CarInfo> getAllCar(Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND status = 'enable' AND license_number LIKE %?1%",
            countQuery = "SELECT COUNT(1) FROM car WHERE is_using = 1 AND status = 'enable' AND license_number LIKE %?1%",
            nativeQuery = true)
    Page<CarInfo> searchCarByLicenseNum(String num, Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND status = 'enable' AND owner_name = ?1",
            countQuery = "SELECT COUNT(1) FROM car WHERE is_using = 1 AND status = 'enable' AND owner_name = ?1",
            nativeQuery = true)
    Page<CarInfo> searchCarByOwner(String owner, Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE is_using = 1 AND status = 'enable' AND license_number = ?1",
            nativeQuery = true)
    Car getCarByLicenseNum(String licenseNum);

    @Modifying
    @Query(value = "UPDATE car SET status = ?2 WHERE id = ?1",
            nativeQuery = true)
    int updateCarStatus(int id, String status);

}
