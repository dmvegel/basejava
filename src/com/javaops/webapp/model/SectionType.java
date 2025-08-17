package com.javaops.webapp.model;

import java.util.List;

public enum SectionType {
    PERSONAL("Личные качества", textConverter()),
    OBJECTIVE("Позиция", textConverter()),
    ACHIEVEMENT("Достижения", listConverter()),
    QUALIFICATIONS("Квалификация", listConverter()),
    EXPERIENCE("Опыт работы", null),
    EDUCATION("Образование", null);

    private final String title;

    private final Converter converter;

    SectionType(String title, Converter converter) {
        this.title = title;
        this.converter = converter;
    }

    public String getTitle() {
        return title;
    }

    public Section convert(String value) {
        return converter == null ? null : converter.convert(value);
    }

    public String convert(Section value) {
        return converter == null ? null : converter.convert(value);
    }

    private interface Converter {
        String convert(Section section);

        Section convert(String value);
    }

    private static Converter textConverter() {
        return new Converter() {
            @Override
            public String convert(Section section) {
                return ((TextSection) section).getText();
            }

            @Override
            public Section convert(String value) {
                return new TextSection(value);
            }
        };
    }

    private static Converter listConverter() {
        return new Converter() {
            @Override
            public String convert(Section section) {
                return String.join("\n", ((ListSection) section).getTexts());
            }

            @Override
            public Section convert(String value) {
                return new ListSection(List.of(value.split("\n")));
            }
        };
    }
}
