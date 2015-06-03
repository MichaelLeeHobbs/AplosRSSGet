package com.aplos.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to handle properties files
 * Project: GasStation
 * Created by Michael Hobbs on 3/21/15.
 * email: Michael.Lee.Hobbs@gmail.com
 */
public class PropertiesUtil {
    /**
     * Loads a properties file from and internal resource file and returns it as a Properties object
     * @param internalResourceFileName an internal resource file containing properties
     * @return Properties
     * @throws java.io.IOException if file not found
     */
    public Properties loadProperties(String internalResourceFileName) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(internalResourceFileName);
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + internalResourceFileName + "' not found in the classpath.");
        }

        return  properties;
    }

    public Properties loadPropertiesExternal(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);

        return  properties;
    }
}
