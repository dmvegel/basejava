package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {
    Resume doRead(InputStream is) throws IOException;

    void doWrite(Resume r, OutputStream os) throws IOException;
}
