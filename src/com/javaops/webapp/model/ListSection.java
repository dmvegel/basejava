package com.javaops.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private List<String> texts;

    public ListSection() {
    }

    public ListSection(List<String> texts) {
        this.texts = texts;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "texts=" + texts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(texts, that.texts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(texts);
    }
}
