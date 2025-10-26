package com.luzhu.truck.dto.car;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateOwnerStatusParam {
    @NotNull(message = "車主ID不可為空")
    private int id;
    
    @Pattern(regexp = "^(enable|disable)$", message = "狀態只能是 enable 或 disable")
    private String status;
}
