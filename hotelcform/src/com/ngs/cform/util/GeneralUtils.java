package com.ngs.cform.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class GeneralUtils {
	
	public static Properties loadProperties(String fileClassPath) throws IOException {
        Properties props = new Properties();
        System.out.println("111111" + fileClassPath);
        InputStream is = GeneralUtils.class.getResourceAsStream(fileClassPath);
        props.load(is);
        return props;
    }
	
	public static URL getResourcePath(String path) {
		return GeneralUtils.class.getResource(path);
	}
}
