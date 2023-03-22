package com.nutri.rest.utils;

public enum OrderStatus {
    ORDER_STATUS_1("Ordered"),
    ORDER_STATUS_2("Restaurant Confirmed Order"),
    ORDER_STATUS_3("Order Preparation in progress"),
    ORDER_STATUS_4("Delivery Person assigned"),
    ORDER_STATUS_5("Delivery in progress"),
    ORDER_STATUS_6("Order Delivered");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}