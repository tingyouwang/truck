package com.luzhu.truck.dto.car;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;


//@Getter
public interface CarInfo {
    long getId();
    String getLicenseNumber();
    String getOwnerName();
}
