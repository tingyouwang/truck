package com.luzhu.truck.dto.caragency;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateCarAgencyStatusParam {
    @NotNull(message = "車行ID不可為空")
    private int id;
    
    @Pattern(regexp = "^(enable|disable)$", message = "狀態只能是 enable 或 disable")
    private String status;
}
