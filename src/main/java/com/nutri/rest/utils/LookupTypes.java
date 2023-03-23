package com.nutri.rest.utils;

public enum LookupTypes {
    ORDER_STATUS_TYPE(9l);

    private final Long value;

    LookupTypes(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
