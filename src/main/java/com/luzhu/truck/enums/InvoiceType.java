package com.luzhu.truck.enums;

public enum InvoiceType {
    //抵油單
    GAS("GAS"),
    SALE("SALE"),
    OFFSET("OFFSET");
    private String type;
    InvoiceType(String type) {
        this.type = type;
    }
    public String getType() {
        return this.type;
    }
}
