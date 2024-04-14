package com.luzhu.truck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemExceptionEnum implements IRuntimeExceptionEnum{
    PARAM_ERROR("SYS_0001", "PARAM_ERROR", "參數錯誤"),
    INSERT_ERROR("SYS_0002", "INSERT_ERROR", "新增失敗"),
    UPDATE_ERROR("SYS_0003", "UPDATE_ERROR", "修改失敗"),
    DELETE_ERROR("SYS_0004", "DELETE_ERROR", "刪除失敗"),
    DATA_ERROR("SYS_0005", "DATA_ERROR", "資料錯誤"),
    NO_DATA("SYS_0006", "NO_DATA", "查無資料"),
    DUPLICATE_DATA("SYS_0007", "DUPLICATE_DATA", "資料重覆"),

    NOT_IMPLEMENTED_METHOD("SYS_8888", "NOT_IMPLEMENTED_METHOD", "未實作方法"),

    SYSTEM_ERROR("SYS_9999", "UNKNOWN_FATAL_ERROR", "系統錯誤"),
    ;

    private String code;
    private String message;
    private String memo;

}
