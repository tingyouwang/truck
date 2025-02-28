package com.luzhu.truck.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "car")
@Data
public class Car {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "license_number", nullable = false, length = 50, unique = true)
    private String licenseNumber;
    private Integer isUsing;

    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;

    @Column(name = "car_agency", nullable = false, length = 100)
    private String carAgency;
    @Column(name = "car_agency_id", nullable = false, length = 100)
    private int carAgencyId;

    @Column(name = "join_date", nullable = false)
//    @Temporal(TemporalType.DATE)
//    private Date joinDate;
    private String joinDate;

    @Column(name = "quit_date")
//    @Temporal(TemporalType.DATE)
    private String quitDate;

    @Column(name = "join_amount")
    private Double joinAmount;

    @Column(name = "quit_amount")
    private Double quitAmount;

    @Column(name = "car_from", length = 300)
    private String carFrom;

    @Column(name = "quit_place", length = 300)
    private String quitPlace;

    @Column(name = "license_issue_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private String licenseIssueDate;

    @Column(name = "manufacture_year_month", nullable = false)
//    @Temporal(TemporalType.DATE)
    private String manufactureYearMonth;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "cc", nullable = false, length = 100)
    private String cc;

    @Column(name = "engine_num", nullable = false, length = 100)
    private String engineNum;

    @Column(name = "inspection_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private String inspectionDate;

    @Column(name = "renew_license_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private String renewLicenseDate;

    @Column(name = "car_type_outlooking", nullable = false, length = 50)
    private String carTypeOutlooking;

    @Column(name = "pass_license", length = 50)
    private String passLicense;

    @Column(name = "car_weight", length = 50)
    private String carWeight;

    @Column(name = "loading_weight", length = 50)
    private String loadingWeight;

    @Column(name = "car_type", nullable = false, length = 50)
    private String carType;

    @Column(name = "inspection_type", nullable = false)
    private BigDecimal inspectionType;

    @Column(name = "violation_date")
//    @Temporal(TemporalType.DATE)
    private String violationDate;

    @Column(name = "report_stop_date")
//    @Temporal(TemporalType.DATE)
    private String reportStopDate;

    @Column(name = "report_scrap_date")
//    @Temporal(TemporalType.DATE)
    private String reportScrapDate;

    @Column(name = "old_license_number", length = 50)
    private String oldLicenseNumber;

    @Column(name = "note1", nullable = false, length = 500)
    private String note1;

    @Column(name = "note2", nullable = false, length = 500)
    private String note2;
    private Double ton;
}
