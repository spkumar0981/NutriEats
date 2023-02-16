package com.nutri.rest.utils;

public enum SubscriptionFrequency {
    FREQUENCY_1("WEEKLY"),
    FREQUENCY_2("MONTHLY");

    private final String value;

    SubscriptionFrequency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
