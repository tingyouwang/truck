package com.luzhu.truck.constant;

public class ExpireTimeConst {
    // 後台密碼重置失效時間
    public static final int ADMIN_RESET_PASSWORD_TIME = 20;

    // 後台token失效時間 單位:分鐘
    public static final int ADMIN_TOKEN_TIME = 1 * 60;
    // 後台ws token失效時間 單位:天
    public static final int ADMIN_WS_TOKEN_TIME = 7;
    // 會員token失效時間 單位:分鐘
    public static final int MEMBER_TOKEN_TIME = 5 * 60;

    // google驗證碼失效時間
    public static final int GOOGLE_AUTH_EXPIRE_TIME = 5;

    // 已通過驗證碼 保留10分鐘
    public static final int VERIFY_CODE_PASSED = 10;
    // 驗證碼錯誤鎖失效時間
    public static final int VERIFY_CODE_LOCK = 5;
    // 註冊驗證碼錯誤鎖失效時間
    public static final int REGISTER_VERIFY_CODE_LOCK = 15;
    // 註冊驗證碼失效時間 單位:分鐘
    public static final int REGISTER_VERIFY_CODE = 30;
    // 驗證碼失效時間 單位:分鐘
    public static final int VERIFY_CODE = 30;
}
