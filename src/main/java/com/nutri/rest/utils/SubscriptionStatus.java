package com.nutri.rest.utils;

public enum SubscriptionStatus {
    SUBSCRIPTION_STATUS_1("Customer Subscribed"),
    SUBSCRIPTION_STATUS_2("Menu Created by Dietitian"),
    SUBSCRIPTION_STATUS_3("Menu Rejected"),
    SUBSCRIPTION_STATUS_4("Menu Confirmed"),
    SUBSCRIPTION_STATUS_5("Ordered");

    private final String value;

    SubscriptionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}