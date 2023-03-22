package com.nutri.rest.utils;

public enum RecurringOrderStatus {
    REC_ORDER_STATUS_1("Ordered"),
    REC_ORDER_STATUS_2("Amount quoted by restaurant"),
    REC_ORDER_STATUS_3("Dietitian confirms the order"),
    REC_ORDER_STATUS_4("Restaurant confirms the order");

    private final String value;

    RecurringOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}