package com.luzhu.truck.util;

/**
 * other_lend_money.type：NORMAL 一般列、ADJUSTMENT 報廢轉月回沖列。
 */
public final class OtherLendMoneyTypeUtil {

    public static final String NORMAL = "NORMAL";
    public static final String ADJUSTMENT = "ADJUSTMENT";

    private OtherLendMoneyTypeUtil() {
    }

    public static boolean isAdjustmentRow(String type) {
        return ADJUSTMENT.equalsIgnoreCase(type);
    }

    public static boolean isAllowedManualType(String type) {
        if (type == null || type.isBlank()) {
            return true;
        }
        return NORMAL.equalsIgnoreCase(type);
    }

    public static String toAdjustmentType() {
        return ADJUSTMENT;
    }
}
