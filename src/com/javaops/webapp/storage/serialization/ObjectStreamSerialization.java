package com.javaops.webapp.storage.serialization;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;

import java.io.*;

public class ObjectStreamSerialization implements SerializationStrategy {
    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            try {
                return (Resume) ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new StorageException("Error read resume", null, e);
            }
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        }
    }
}
