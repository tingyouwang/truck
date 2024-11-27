package com.luzhu.truck.dto.car;

import com.luzhu.truck.dto.BaseParam;
import lombok.Data;

@Data
public class SearchCarLicenseNumParam extends BaseParam {
    private String licenseNumber;
}
