package com.javaops.webapp.model;

import java.util.Arrays;

public enum SectionType {
    PERSONAL("Личные качества", new TextConverter()),
    OBJECTIVE("Позиция", new TextConverter()),
    ACHIEVEMENT("Достижения", new ListConverter()),
    QUALIFICATIONS("Квалификация", new ListConverter()),
    EXPERIENCE("Опыт работы", new CompanyConverter()),
    EDUCATION("Образование", new CompanyConverter());

    private final String title;
    private final Converter converter;

    SectionType(String title, Converter converter) {
        this.title = title;
        this.converter = converter;
    }

    public String getTitle() {
        return title;
    }

    interface Converter {
        String convert(Section section);

        Section convert(String value);
    }

    static class TextConverter implements Converter {
        @Override
        public String convert(Section section) {
            return ((TextSection) section).getText();
        }

        @Override
        public Section convert(String value) {
            return new TextSection(value.replace("\n", "").replace("\r", " "));
        }
    }

    static class ListConverter implements Converter {
        @Override
        public String convert(Section section) {
            return String.join("\n", ((ListSection) section).getTexts());
        }

        @Override
        public Section convert(String value) {
            return new ListSection(Arrays.stream(value.split("\n")).filter(s -> !s.isBlank()).toList());
        }
    }

    static class CompanyConverter implements Converter {
        @Override
        public String convert(Section section) {
            return null;
        }

        @Override
        public Section convert(String value) {
            return null;
        }
    }

    public String convert(Section section) {
        return converter.convert(section);
    }

    public Section convert(String value) {
        return converter.convert(value);
    }
}
