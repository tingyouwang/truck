package com.luzhu.truck.dto.car;

import com.luzhu.truck.annotation.JpaDto;
import lombok.Data;

@Data
@JpaDto
public class CarOwnerDropDownDto {
    private long id;
    private String name;
}
