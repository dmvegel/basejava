package com.javaops.webapp;

import com.javaops.webapp.model.Resume;
import com.javaops.webapp.storage.AbstractArrayStorage;
import com.javaops.webapp.storage.ArrayStorage;
import com.javaops.webapp.storage.SortedArrayStorage;
import com.javaops.webapp.storage.Storage;

/**
 * Test for your ArrayStorage implementation
 */
public class MainTestArrayStorage {
    public static void main(String[] args) {
        AbstractArrayStorage[] storages = new AbstractArrayStorage[]{new ArrayStorage(), new SortedArrayStorage()};
        for (Storage storage : storages) {
            for (int i = 0; i <= storage.size(); i++) {
                Resume r = new Resume();
                r.setUuid("uuid" + i);
                storage.save(r);
            }
            storage.clear();

            Resume r1 = new Resume();
            r1.setUuid("uuid1");
            Resume r2 = new Resume();
            r2.setUuid("uuid2");
            Resume r3 = new Resume();
            r3.setUuid("uuid3");
            Resume r4 = new Resume();
            r4.setUuid("uuid2");
            Resume r5 = new Resume();
            r5.setUuid("uuid5");

            storage.save(r2);
            storage.save(r1);
            storage.save(r3);
            storage.save(r3);

            storage.update(r4);
            storage.update(r5);

            System.out.println("Get r4: " + storage.get(r4.getUuid()));
            System.out.println("Get r1: " + storage.get(r1.getUuid()));
            System.out.println("Size: " + storage.size());

            System.out.println("Get dummy: " + storage.get("dummy"));

            printAll(storage);
            storage.delete(r1.getUuid());
            printAll(storage);
            System.out.println("Size: " + storage.size());
            storage.clear();
            printAll(storage);

            System.out.println("Size: " + storage.size());
        }
    }

    static void printAll(Storage storage) {
        System.out.println("\nGet All");
        for (Resume r : storage.getAll()) {
            System.out.println(r);
        }
    }
}
