package common.util;


import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
    private static final Config instance = new Config();
    protected String configPath;
    private Config() {}
    public void setPath(String Path) { // sets config file path
        configPath = Path;
        try {
            load(new FileReader(configPath));
        } catch (IOException e) {
            System.out.println("Error Reading Config File : " + configPath);
        }
    }


    public <T> T getProperty(String K, Class<T> c) { // loads a single property from config file
        String value = getProperty(K);
        if (value == null)
            throw new RuntimeException("Config property: " + K + " not set");
        try {
            return c.getConstructor(String.class).newInstance(value);
        } catch (Exception ignored) {}
        return null;
    }

    public static Config getInstance() {
        return instance;
    }
}

