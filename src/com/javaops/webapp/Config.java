package com.javaops.webapp;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final File PROPS_FILE = new File("config\\resumes.properties");

    private final File storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private Config() {
        try (InputStream is = new FileInputStream(PROPS_FILE)) {
            Properties properties = new Properties();
            properties.load(is);
            storageDir = new File(properties.getProperty("storage.dir"));
            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.user");
            dbPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS_FILE.getAbsolutePath());
        }
    }

    private static class ConfigHolder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return ConfigHolder.INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
