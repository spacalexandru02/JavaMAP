package configReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "/Users/alexandruspac/Downloads/demo1/src/main/java/configReader/settings.properties";
    private Properties properties = new Properties();

    public Map<String, String> config() {
        Map<String, String> elements = new HashMap<>();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);

            String repositoryType = properties.getProperty("Repository");
            String patientsFile = properties.getProperty("Pacienti");
            String appointmentsFile = properties.getProperty("Programari");
            String appFile = properties.getProperty("App");

            elements.put("Repository", repositoryType);
            elements.put("Pacienti", patientsFile);
            elements.put("Programari", appointmentsFile);
            elements.put("App", appFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elements;
    }
}
