package com.luzhu.truck.dto.loanfeesetting;

import com.luzhu.truck.dto.BaseParam;
import lombok.Data;

@Data
public class QueryAllByCarLicenseNumParam extends BaseParam {
    private String carLicenseNum;
}
