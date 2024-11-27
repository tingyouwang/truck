package com.luzhu.truck.dto.car;

import com.luzhu.truck.dto.BaseParam;
import lombok.Data;

@Data
public class SearchCarOwnerParam extends BaseParam {
    private String searchName;
}
