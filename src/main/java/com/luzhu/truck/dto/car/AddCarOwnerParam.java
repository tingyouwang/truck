package com.luzhu.truck.dto.car;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCarOwnerParam {
    // Fields from the 'owner' table
    @NotBlank(message = "車主姓名必填")
    private String name;
    private String idNum;
    private String sex;
    private String birthday;
    @NotBlank(message = "車主電話1必填")
    private String phone1;
    private String phone2;
    private String mobile;
    private String fax;
    private String address;
    private String mailAddress;
}
