package com.javaops.webapp.model;

import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {
    private List<CompanyBlock> companyBlocks;

    public CompanySection() {
    }

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
