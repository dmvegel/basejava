package com.javaops.webapp.storage.serialization;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DataStreamSerialization implements SerializationStrategy {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            fillMap(dis, in -> {
                ContactType type = ContactType.valueOf(in.readUTF());
                String value = in.readUTF();
                resume.getContacts().put(type, value);
            });

            fillMap(dis, in -> {
                SectionType type = SectionType.valueOf(in.readUTF());
                resume.getSections().put(type, readSection(in, type));
            });

            return resume;
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            writeList(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            writeList(dos, r.getSections().entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                writeSection(dos, sectionType, entry.getValue());
            });
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) {
        try {
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> {
                    return readTextSection(dis);
                }
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    return readListSection(dis);
                }
                case EDUCATION, EXPERIENCE -> {
                    return readCompanySection(dis);
                }
                default -> {
                    throw new StorageException(null, "Wrong section type");
                }
            }
        } catch (IOException e) {
            throw new StorageException(null, "Section reading error");
        }
    }

    private TextSection readTextSection(DataInputStream dis) throws IOException {
        TextSection textSection = new TextSection();
        textSection.setText(dis.readUTF());
        return textSection;
    }

    private ListSection readListSection(DataInputStream dis) throws IOException {
        ListSection listSection = new ListSection();
        listSection.setTexts(readList(dis, dis::readUTF));
        return listSection;
    }

    private CompanySection readCompanySection(DataInputStream dis) throws IOException {
        CompanySection companySection = new CompanySection();
        companySection.setBlocks(readList(dis, () -> readBlock(dis)));
        return companySection;
    }

    private CompanyBlock readBlock(DataInputStream dis) throws IOException {
        CompanyBlock companyBlock = new CompanyBlock();
        companyBlock.setTitle(dis.readUTF());
        companyBlock.setUrl(nullIfEmpty(dis.readUTF()));
        companyBlock.setPeriods(readList(dis, () -> readPeriod(dis)));
        return companyBlock;
    }

    private Period readPeriod(DataInputStream dis) throws IOException {
        Period period = new Period();
        period.setStart(LocalDate.parse(dis.readUTF(), FORMATTER));
        period.setEnd(LocalDate.parse(dis.readUTF(), FORMATTER));
        period.setTitle(dis.readUTF());
        period.setText(nullIfEmpty(dis.readUTF()));
        return period;
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, Section section) {
        try {
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> {
                    writeTextSection(dos, ((TextSection) section));
                }
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    writeListSection(dos, (ListSection) section);
                }
                case EDUCATION, EXPERIENCE -> {
                    writeCompanySection(dos, (CompanySection) section);
                }
            }
        } catch (IOException e) {
            throw new StorageException(null, "Section writing error");
        }
    }

    private void writeTextSection(DataOutputStream dos, TextSection testSection) throws IOException {
        dos.writeUTF(testSection.getText());
    }

    private void writeListSection(DataOutputStream dos, ListSection listSection) throws IOException {
        writeList(dos, listSection.getTexts(), dos::writeUTF);
    }

    private void writeCompanySection(DataOutputStream dos, CompanySection companySection) throws IOException {
        writeList(dos, companySection.getBlocks(), (block) -> writeBlock(dos, block));
    }

    private void writeBlock(DataOutputStream dos, CompanyBlock companyBlock) throws IOException {
        dos.writeUTF(companyBlock.getTitle());
        dos.writeUTF(Optional.ofNullable(companyBlock.getUrl()).orElse(""));

        writeList(dos, companyBlock.getPeriods(), (period) -> writePeriod(dos, period));
    }

    private void writePeriod(DataOutputStream dos, Period period) throws IOException {
        dos.writeUTF(period.getStart().format(FORMATTER));
        dos.writeUTF(period.getEnd().format(FORMATTER));
        dos.writeUTF(period.getTitle());
        dos.writeUTF(Optional.ofNullable(period.getText()).orElse(""));
    }

    private String nullIfEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    private <T> List<T> readList(DataInputStream dis, IOSupplier<T> supplier) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    private <T> void writeList(DataOutputStream dos, Collection<T> elements, IOConsumer<T> consumer) throws IOException {
        dos.writeInt(elements.size());
        for (T element : elements) {
            consumer.accept(element);
        }
    }

    private void fillMap(DataInputStream dis, IOConsumer<DataInputStream> consumer) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            consumer.accept(dis);
        }
    }

    @FunctionalInterface
    private interface IOSupplier<T> {
        T get() throws IOException;
    }

    @FunctionalInterface
    private interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }
}
