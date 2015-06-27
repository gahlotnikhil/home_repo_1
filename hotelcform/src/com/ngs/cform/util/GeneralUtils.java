package com.ngs.cform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GeneralUtils {
	
	public static Properties loadProperties(String path, boolean isAbsolute) throws IOException {
        Properties props = new Properties();
        if (isAbsolute) {
        	props.load(new FileInputStream(path));
        } else {
        	props.load(getResourceAsStream(path));
        }
        return props;
    }
	
//	public static URL getResourcePath(String path) {
//		return GeneralUtils.class.getResource(path);
//	}
	
	public static InputStream getResourceAsStream(String path) throws FileNotFoundException {
		InputStream is = GeneralUtils.class.getResourceAsStream(path);
		if (is == null) {
			is = new FileInputStream(path);
		}
		return is;
	}
	
	public static Path copyFileToDir(InputStream inputStream, String dirPath) {
		try {
			Path path = Paths.get(dirPath);
			//Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
			if (!new File(path.toUri()).exists()) {
				Files.copy(inputStream, path);
			}
			return path;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void storeProperties(String path, Properties props) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(path);
			props.store(out, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
