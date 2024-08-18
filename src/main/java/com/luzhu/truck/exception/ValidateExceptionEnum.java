package com.luzhu.truck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidateExceptionEnum implements IRuntimeExceptionEnum{
    PARAM_ERROR("VALID_0001", "PARAM_ERROR", "年月格式錯誤"),
    ;

    private String code;
    private String message;
    private String memo;

}
