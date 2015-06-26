package com.ngs.cform.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.ngs.cform.field.FormFieldGenerator;
import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.manager.WordManager;
import com.ngs.cform.resource.ResourceConfig;

public class ConfigSession {
	
	private static ConfigSession session = new ConfigSession();
	
	private ExcelDataManager dataManager;
    private WordManager wordManager;
    private FormFieldGenerator fieldGenerator;
    private Properties configProperties;
    private ResourceConfig resourceConfig;
    private Properties properties;
	
	private ConfigSession() {
		loadConfigFiles();
	}
	
	public static ConfigSession getConfigSession() {
		if (session == null) {
			session = loadSession();
		}
		return session;
	}
	
	public static ConfigSession loadSession() {
		if (session == null) {
			session = new ConfigSession();
		}
		
		return session;
	}

	private void loadConfigFiles() {
		try {
			// TODO use this file from outside the application eg: /home/ngs-app-data/config.properties
			String storePath = createStoreAtHome();
			// Store all the files in the same location as default
			// User defined values should override values in ~home/config.properties
			// Defaults will always be there in the config.properties as part of the application.
			System.out.println(System.getProperty("user.home") + "vvvvvvvvvvvvv");
			configProperties = GeneralUtils.loadProperties("../config.properties");
			
			resourceConfig = new ResourceConfig(configProperties.get("resourceXML") != null ? String.valueOf(configProperties.get("xlDataFile")) : null);
			
			properties = GeneralUtils.loadProperties("../resource.properties");
			
			File file = new File(String.valueOf(configProperties.get("xlDataFile")));
			dataManager = new ExcelDataManager(file, resourceConfig);
			wordManager = new WordManager(new File(String.valueOf(configProperties.get("wordFormatFile"))), properties);
			
			fieldGenerator = new FormFieldGenerator(resourceConfig, properties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String createStoreAtHome() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExcelDataManager getDataManager() {
		return dataManager;
	}

	public WordManager getWordManager() {
		return wordManager;
	}

	public FormFieldGenerator getFieldGenerator() {
		return fieldGenerator;
	}

	public Properties getConfigProperties() {
		return configProperties;
	}

	public ResourceConfig getResourceConfig() {
		return resourceConfig;
	}

	public Properties getProperties() {
		return properties;
	}
	
}
