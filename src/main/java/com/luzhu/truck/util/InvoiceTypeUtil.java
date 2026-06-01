package com.luzhu.truck.util;

import com.luzhu.truck.dto.car.CarFeeJoinInvoiceDto;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.enums.InvoiceType;

import java.util.EnumMap;
import java.util.Map;

/**
 * invoice.type：基底 SALE/OFFSET/GAS 與回沖列 *_ADJUSTMENT；稅率與帳單分桶皆以「基底類型」為準。
 */
public final class InvoiceTypeUtil {

    private static final String SUFFIX = "_ADJUSTMENT";

    private static final Map<InvoiceType, String> BASE_TO_ADJUSTMENT = new EnumMap<>(InvoiceType.class);

    static {
        BASE_TO_ADJUSTMENT.put(InvoiceType.SALE, "SALE_ADJUSTMENT");
        BASE_TO_ADJUSTMENT.put(InvoiceType.OFFSET, "OFFSET_ADJUSTMENT");
        BASE_TO_ADJUSTMENT.put(InvoiceType.GAS, "GAS_ADJUSTMENT");
    }

    private InvoiceTypeUtil() {
    }

    public static boolean isAdjustmentRow(String type) {
        if (type == null) {
            return false;
        }
        return type.endsWith(SUFFIX);
    }

    /** 若已是 ADJUSTMENT 列則回傳基底字串，否則回傳原字串大寫。 */
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
        if (baseType == null) {
            throw new IllegalArgumentException("baseType is null");
        }
        String u = baseType.toUpperCase();
        return switch (u) {
            case "SALE" -> "SALE_ADJUSTMENT";
            case "OFFSET" -> "OFFSET_ADJUSTMENT";
            case "GAS" -> "GAS_ADJUSTMENT";
            default -> throw new IllegalArgumentException("not a base invoice type: " + baseType);
        };
    }

    /** 手動立帳僅允許三種基底。 */
    public static boolean isAllowedManualType(String type) {
        if (type == null) {
            return false;
        }
        String u = type.toUpperCase();
        return "SALE".equals(u) || "OFFSET".equals(u) || "GAS".equals(u);
    }

    public static double taxPercentForCarFee(CarFee carFee, String rowType) {
        return switch (baseType(rowType)) {
            case "SALE" -> carFee.getSaleTax() != null ? carFee.getSaleTax() : 0.0;
            case "GAS" -> carFee.getGasTax() != null ? carFee.getGasTax() : 0.0;
            case "OFFSET" -> carFee.getBuyTax() != null ? carFee.getBuyTax() : 0.0;
            default -> 0.0;
        };
    }

    public static double taxPercentForJoinDto(CarFeeJoinInvoiceDto dto, String rowType) {
        return switch (baseType(rowType)) {
            case "SALE" -> dto.getSaleTax() != null ? dto.getSaleTax() : 0.0;
            case "GAS" -> dto.getGasTax() != null ? dto.getGasTax() : 0.0;
            case "OFFSET" -> dto.getBuyTax() != null ? dto.getBuyTax() : 0.0;
            default -> 0.0;
        };
    }

    public static InvoiceType invoiceTypeBucket(String rowType) {
        return switch (baseType(rowType)) {
            case "GAS" -> InvoiceType.GAS;
            case "SALE" -> InvoiceType.SALE;
            case "OFFSET" -> InvoiceType.OFFSET;
            default -> throw new IllegalArgumentException("unknown invoice type: " + rowType);
        };
    }

    public static String adjustmentTypeForBucket(InvoiceType bucket) {
        String adj = BASE_TO_ADJUSTMENT.get(bucket);
        if (adj == null) {
            throw new IllegalArgumentException("no adjustment for bucket " + bucket);
        }
        return adj;
    }
}
