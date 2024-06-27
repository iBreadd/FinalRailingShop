package com.example.RailingShop.Enums;

public enum ProductColor {
    BLACK("Черен"),
    BROWN("Кафяв"),
    WHITE("Бял"),
    GOLDEN("Златист"),
    SILVER("Сребърен");

    private final String label;

    ProductColor(String label){
        this.label=label;
    }

    public String getLabel() {
        return label;
    }
}
