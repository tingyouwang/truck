package com.luzhu.truck.dto.car;

import com.luzhu.truck.annotation.JpaDto;
import lombok.Data;

@Data
@JpaDto
public class CarFeeJoinInvoiceDto {
    private String carLicenseNum;
    private Double saleTax;
    private Double buyTax;
    private Double gasTax;
    private Double oweTax;
    private Double receipTax;
    private String type;
    private Integer disable;
    /** 西元 yyyy-MM-dd */
    private String handleDate;
    private Integer rebillSourceInvoiceId;
    private Integer rebillTargetInvoiceId;
}
