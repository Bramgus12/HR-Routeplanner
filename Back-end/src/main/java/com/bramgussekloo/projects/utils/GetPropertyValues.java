package com.bramgussekloo.projects.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetPropertyValues {
    private static InputStream inputStream;

    /**
     * Get values from properties file
     * Resource: https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
     */
    public static String[] getDatabasePropValues(String propFileName) {
        String[] result = {"", "", "", "", ""};
        try {
            Properties prop = new Properties();
            inputStream = GetPropertyValues.class.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file " + propFileName + " found.");
            }
            String db_url = prop.getProperty("db_url");
            String db_username = prop.getProperty("db_username");
            String db_password = prop.getProperty("db_password");
            String db_url_short = prop.getProperty("db_url_short");
            String db_name = prop.getProperty("db_name");
            result[0] = db_url;
            result[1] = db_username;
            result[2] = db_password;
            result[3] = db_url_short;
            result[4] = db_name;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static File getResourcePath(String FolderName, String fileName) throws IOException {
        Properties properties = new Properties();
        inputStream = GetPropertyValues.class.getClassLoader().getResourceAsStream("file_path.properties");
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new IOException("Property file file_path.properties not found");
        }
        if (!fileName.isEmpty()) {
            String file_path = properties.getProperty("file_path") + FolderName + "/" + fileName;
            return new File(file_path);
        } else {
            String file_path = properties.getProperty("file_path") + FolderName;
            return new File(file_path);
        }
    }
}
