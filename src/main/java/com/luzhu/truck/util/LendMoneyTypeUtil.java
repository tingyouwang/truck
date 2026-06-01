package com.luzhu.truck.util;

/**
 * lend_money.type：基底 CHECK/CASH 與回沖列 *_ADJUSTMENT。
 */
public final class LendMoneyTypeUtil {

    private static final String SUFFIX = "_ADJUSTMENT";

    private LendMoneyTypeUtil() {
    }

    public static boolean isAdjustmentRow(String type) {
        if (type == null) {
            return false;
        }
        return type.toUpperCase().endsWith(SUFFIX);
    }

    public static String baseType(String type) {
        if (type == null) {
            return "";
        }
        String u = type.toUpperCase();
        if (u.endsWith(SUFFIX)) {
            return u.substring(0, u.length() - SUFFIX.length());
        }
        return u;
    }

    public static String toAdjustmentType(String baseType) {
        String u = baseType(baseType);
        return switch (u) {
            case "CHECK" -> "CHECK_ADJUSTMENT";
            case "CASH" -> "CASH_ADJUSTMENT";
            default -> throw new IllegalArgumentException("not a base lend money type: " + baseType);
        };
    }

    public static boolean isAllowedManualType(String type) {
        if (type == null) {
            return false;
        }
        String u = type.toUpperCase();
        return "CHECK".equals(u) || "CASH".equals(u);
    }
}
