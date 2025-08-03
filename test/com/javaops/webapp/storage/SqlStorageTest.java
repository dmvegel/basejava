package com.javaops.webapp.storage;

import com.javaops.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    SqlStorageTest() {
        Config cfg = Config.getInstance();
        super(new SqlStorage(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword()));
    }
}
