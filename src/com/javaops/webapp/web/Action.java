package com.javaops.webapp.web;

public enum Action {
    CREATE, VIEW, EDIT, DELETE;

    public static Action parseAction(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Action.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Action " + value + " is illegal");
        }
    }
}
