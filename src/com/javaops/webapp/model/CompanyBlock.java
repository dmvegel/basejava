package com.javaops.webapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CompanyBlock implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String url;
    private List<Period> periods;

    public CompanyBlock() {
    }

    public CompanyBlock(String title, String url, List<Period> periods) {
        this.periods = periods;
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return "CompanyBlock{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", periods=" + periods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompanyBlock that = (CompanyBlock) o;
        return Objects.equals(title, that.title) && Objects.equals(url, that.url) && Objects.equals(periods, that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, periods);
    }
}
