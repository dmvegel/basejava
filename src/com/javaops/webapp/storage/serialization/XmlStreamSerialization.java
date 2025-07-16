package com.javaops.webapp.storage.serialization;

import com.javaops.webapp.model.*;
import com.javaops.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerialization implements SerializationStrategy {
    private XmlParser xmlParser;

    public XmlStreamSerialization() {
        xmlParser = new XmlParser(
                Resume.class, CompanySection.class, CompanyBlock.class,
                ListSection.class, Period.class, TextSection.class
        );
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, w);
        }
    }
}
