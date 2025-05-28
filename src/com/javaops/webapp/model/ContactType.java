package com.javaops.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Телефон"),
    SKYPE("Skype"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private final String typeName;

    ContactType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
