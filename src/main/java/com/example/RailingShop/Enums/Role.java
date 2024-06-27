package com.example.RailingShop.Enums;

public enum Role {
    ADMIN("ADMIN",1),
    USER("USER",2);
    Role(String label, int accessLevel){
        this.label = label;
        this.accessLevel = accessLevel;
    }

    private int accessLevel;
    private String label;

    @Override
    public String toString() {
        return this.label;
    }
}
