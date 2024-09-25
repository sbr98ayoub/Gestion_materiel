package com.example.my_parc.model;

public enum Status {
    IN_STOCK("In Stock"),
    OUT_OF_STOCK("Out of Stock");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
