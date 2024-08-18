package com.luzhu.truck.dto.invoice;

import java.math.BigDecimal;

public interface InvoiceSumAmountAndTaxDto {
    BigDecimal getSum();
    BigDecimal getTaxSum();

}
