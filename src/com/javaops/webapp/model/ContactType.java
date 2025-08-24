package com.javaops.webapp.model;

import com.javaops.webapp.util.HtmlHelper;

public enum ContactType {
    PHONE_NUMBER("Телефон") {
        @Override
        public String convertToString(String value) {
            return getTypeName() + ": " + value;
        }
    },
    SKYPE("Skype") {
        @Override
        public String convertToString(String value) {
            return HtmlHelper.toLink(getTypeName(), "skype:" + value);
        }
    },
    EMAIL("Почта") {
        @Override
        public String convertToString(String value) {
            return HtmlHelper.toLink(getTypeName(), "mailto:" + value);
        }
    },
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

    public String convertToString(String value) {
        return HtmlHelper.toLink(getTypeName(), value);
    }
}
