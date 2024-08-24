package com.luzhu.truck.dto.invoice;

import com.luzhu.truck.dto.BaseParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GetInvoiceParam extends BaseParam {
    @NotBlank(message = "type不可為空")
    //SALE:銷發, OFFSET:抵發, GAS:抵油
    @Pattern(regexp = "SALE|OFFSET|GAS", flags = Pattern.Flag.UNICODE_CASE)
    private String type;
    @NotBlank(message = "車牌不可為空")
    private String carLicenseNum;
    @NotBlank(message = "帳單月份")
    private String expenseYearMonth;
}
