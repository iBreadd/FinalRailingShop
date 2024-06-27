package com.example.RailingShop.Enums;

public enum ProductMaterial {
    ALUMINUM("Алуминиеви"),
    STEEL("Стоманени"),
    STAINLESS_STEEL("Иноксови"),
    WROUGHT_IRON("Ковано желязо"),
    GLASS("Стъклени"),
    BRASS("Месингови"),
    WOODEN("Дървени"),
    DESIGNER("Дизайнерски");
    private final String label;

    ProductMaterial(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
