package com.javaops.webapp.model;

import java.time.LocalDate;
import java.util.*;

public class Resume implements Comparable<Resume> {
    private String uuid;

    private String fullName;

    private Map<ContactType, String> contacts;

    private Map<SectionType, Section> sections;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "name must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
        sections = new HashMap<>();
        contacts = new HashMap<>();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public void setSections(Map<SectionType, Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    @Override
    public int compareTo(Resume o) {
        return this.uuid.compareTo(o.getUuid());
    }

    public abstract static class Section {
    }

    public static class TextSection extends Section {
        private String text;

        public TextSection(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "TextSection{" +
                    "text='" + text + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TextSection that = (TextSection) o;
            return Objects.equals(text, that.text);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(text);
        }
    }

    public static class ListSection extends Section {
        private List<String> texts;

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

    public static class CompanySection extends Section {
        private List<CompanyBlock> companyBlocks;

        public CompanySection(List<CompanyBlock> companyBlocks) {
            this.companyBlocks = companyBlocks;
        }

        public List<CompanyBlock> getBlocks() {
            return companyBlocks;
        }

        public void setBlocks(List<CompanyBlock> companyBlocks) {
            this.companyBlocks = companyBlocks;
        }

        @Override
        public String toString() {
            return "CompanySection{" +
                    "companyBlocks=" + companyBlocks +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CompanySection that = (CompanySection) o;
            return Objects.equals(companyBlocks, that.companyBlocks);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(companyBlocks);
        }
    }

    public static class CompanyBlock {
        private String title;
        private String url;
        private List<Period> periods;

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

    public static class Period {
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
}
