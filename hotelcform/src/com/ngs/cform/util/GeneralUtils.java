package com.ngs.cform.util;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class GeneralUtils {
	
	public static Properties loadProperties(String fileClassPath) throws IOException {
        Properties props = new Properties();
        props.load(GeneralUtils.class.getResourceAsStream(fileClassPath));
        return props;
    }
	
	public static URL getResourcePath(String path) {
		return GeneralUtils.class.getResource(path);
	}
}
