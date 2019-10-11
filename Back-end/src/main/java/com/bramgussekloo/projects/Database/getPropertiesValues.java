package com.bramgussekloo.projects.Database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class getPropertiesValues {
    InputStream inputStream;

    public getPropertiesValues() {

    }

    /**
     * Get values from properties file
     * Resource: https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
     */
    public String[] getPropValues(String propFileName) throws IOException {
        String[] result = {"", "", ""};
        try {
            Properties prop = new Properties();

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file " + propFileName + " found.");
            }
            String db_url = prop.getProperty("db_url");
            String db_username = prop.getProperty("db_username");
            String db_password = prop.getProperty("db_password");

            result[0] = db_url;
            result[1] = db_username;
            result[2] = db_password;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}
