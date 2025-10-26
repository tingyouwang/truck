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
    PRIMARY_KEY_CONFLICT("SYS_0008", "保險卡號重覆", "PRIMARY_KEY_CONFLICT"),
    CAR_AGENCY_NAME_DUPLICATE("SYS_0009", "車行名稱重覆", "PRIMARY_KEY_CONFLICT"),
    INVOICE_DISABLE("SYS_0010", "發票已作廢, 無法更新", "INVOICE_DISABLE"),
    GENERATE_CURRENT_BILL("SYS_0011", "不可重複產出當月帳單", "CANT_GENERATE_CURRENT_BILL_FOR_MANY_TIME"),
    DUPLICATE_CAR_LICENSE_NUM("SYS_0012", "車牌重複", "DUPLICATE_CAR_LICENSE_NUM"),
    END_DATE_EARLY_THAN_START_DATE("SYS_0013", "結束日不可早於起始日", "END_DATE_EARLY_THAN_START_DATE"),
    NEED_SET_INSURANCE_CAR_FEE("SYS_0014", "管費或保單尚未設定", "NEED_SET_INSURANCE_CAR_FEE"),

    NOT_IMPLEMENTED_METHOD("SYS_8888", "NOT_IMPLEMENTED_METHOD", "未實作方法"),

    SYSTEM_ERROR("SYS_9999", "UNKNOWN_FATAL_ERROR", "系統錯誤"),
    ;

    private String code;
    private String message;
    private String memo;

}
