package com.luzhu.truck.dto.caragency;

import com.luzhu.truck.annotation.JpaDto;
import lombok.Data;

@Data
@JpaDto
public class CarAgencyDropDownDTO {
    private int carAgencyId;
    private String agencyName;
}
