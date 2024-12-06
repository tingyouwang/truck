package com.luzhu.truck.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddCarParam {
    @NotBlank(message = "車牌不可為空")
    private String licenseNumber;
    //    private int isUsing;
    @NotBlank(message = "車主姓名必填")
    private String ownerName;
    @NotNull(message = "車行Id必填")
    private String carAgency;
    @NotBlank(message = "遷入日期必填")
    private String joinDate;
    private String quitDate;
    private Double joinAmount;
    private Double quitAmount;
    private String carFrom;
    private String quitPlace;
    @NotBlank(message = "發照日期必填")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String licenseIssueDate;
    @NotBlank(message = "出廠日期必填")
    @Pattern(regexp = "^\\d{3}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM")
    private String manufactureYearMonth;
//    @NotNull(message = "出廠年分(西元)必填")
//    private Integer westYear;
    @NotBlank(message = "廠牌必填")
    private String brand;
    @NotBlank(message = "噸位必填")
    private String ton;
    @NotBlank(message = "cc數必填")
    private String cc;
    @NotBlank(message = "引擎號碼必填")
    private String engineNum;
    @NotBlank(message = "驗車日期必填")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String inspectionDate;
    @NotBlank(message = "換照日期必填")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{2}$", message = "日期格式錯誤，應為yyy-MM-dd")
    private String renewLicenseDate;
    @NotBlank(message = "車身式樣必填")
    private String carTypeOutlooking;
    //通行證
    private String passLicense;
    @NotBlank(message = "車重必填")
    private String carWeight;
    @NotBlank(message = "載重必填")
    private String loadingWeight;
    @NotBlank(message = "車輛種類必填")
    private String carType;
    @NotNull(message = "驗車方式")
    private Double inspectionType;
    private String violationDate;
    private String reportStopDate;
    private String reportScrapDate;
    private String oldLicenseNumber;
    private String note1;
    private String note2;
}
