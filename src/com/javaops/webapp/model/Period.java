package com.javaops.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private LocalDate start;
    private LocalDate end;
    private String title;
    private String text;

    public Period(LocalDate start, LocalDate end, String title, String text) {
        this(start, end, title);
        this.text = text;
    }

    public Period(LocalDate start, LocalDate end, String title) {
        this.start = start;
        this.end = end;
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Period{" +
                "start=" + start +
                ", end=" + end +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(start, period.start) && Objects.equals(end, period.end) && Objects.equals(title, period.title) && Objects.equals(text, period.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, title, text);
    }
}
