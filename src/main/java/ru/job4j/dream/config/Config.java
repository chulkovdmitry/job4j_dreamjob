package ru.job4j.dream.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    static Properties properties = new Properties();
    private final static Config INSTANCE = new Config();

    public static Config getConfig() {
        return INSTANCE;
    }

    public Config() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
