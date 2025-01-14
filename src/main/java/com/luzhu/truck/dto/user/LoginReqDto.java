package com.luzhu.truck.dto.user;

import com.luzhu.truck.annotation.DecryptParam;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginReqDto {

    @NotBlank
    private String userId;
    @NotBlank
    @DecryptParam
    private String password;
    // google驗證
//    @NotNull
//    private String googleCode;

}
