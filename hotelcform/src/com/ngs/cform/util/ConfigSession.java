package com.ngs.cform.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.ngs.cform.field.FormFieldGenerator;
import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.manager.WordManager;
import com.ngs.cform.resource.ResourceConfig;

public class ConfigSession {
	
	public static String NGS_APP_DATA_DIR = "ngs-app-data";
	
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
			configProperties = copyConfigFilesToStore(storePath);
			
			resourceConfig = new ResourceConfig(configProperties.get(PROPERTY_RESOURCEXML) != null ? String.valueOf(configProperties.get(PROPERTY_RESOURCEXML)) : null);
			
			properties = GeneralUtils.loadProperties(storePath + RESOURCE_PROPERTIES, true);
			
			File file = new File(String.valueOf(configProperties.get(PROPERTY_XLDATAFILE)));
			dataManager = new ExcelDataManager(file, resourceConfig);
			wordManager = new WordManager(new File(String.valueOf(configProperties.get(PROPERTY_WORDFORMATFILE))), properties);
			
			fieldGenerator = new FormFieldGenerator(resourceConfig, properties);
			
			// Initialise log
			new LogConfig(storePath + "app.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String CONFIG_PROPERTIES = "config.properties";
	private String RESOURCE_PROPERTIES = "resource.properties";
	private String PROPERTY_XLDATAFILE = "xlDataFile";
	private String PROPERTY_WORDFORMATFILE = "wordFormatFile";
	private String PROPERTY_RESOURCEXML = "resourceXML";

	private Properties copyConfigFilesToStore(String storePath) throws IOException {
		// Copy following files to the store
		// * config.properties  -- Main configuration file
		// * resource.properties
		// * xlDataFile -- Path to be written in config.properties
		// * wordFormatFile -- Path to be written in config.properties
		// * resourceXML -- Path to be written in config.properties
		Properties defaultConfigProperties = GeneralUtils.loadProperties("config.properties", false);
		
		// config.properties
		GeneralUtils.copyFileToDir(GeneralUtils.getResourceAsStream(CONFIG_PROPERTIES), storePath + CONFIG_PROPERTIES);
		// resource.properties
		GeneralUtils.copyFileToDir(GeneralUtils.getResourceAsStream(RESOURCE_PROPERTIES), storePath + RESOURCE_PROPERTIES);
		// xlDataFile
		GeneralUtils.copyFileToDir(GeneralUtils.getResourceAsStream(defaultConfigProperties.getProperty(PROPERTY_XLDATAFILE)),
				storePath + defaultConfigProperties.getProperty(PROPERTY_XLDATAFILE));
		// wordFormatFile
		GeneralUtils.copyFileToDir(GeneralUtils.getResourceAsStream(defaultConfigProperties.getProperty(PROPERTY_WORDFORMATFILE)),
				storePath + defaultConfigProperties.getProperty(PROPERTY_WORDFORMATFILE));
		// resourceXML
		GeneralUtils.copyFileToDir(GeneralUtils.getResourceAsStream(defaultConfigProperties.getProperty(PROPERTY_RESOURCEXML)),
				storePath + defaultConfigProperties.getProperty(PROPERTY_RESOURCEXML));
		
		// Update config properties
		Properties copiedProperties = GeneralUtils.loadProperties(storePath + CONFIG_PROPERTIES, true);
		copiedProperties.setProperty(PROPERTY_XLDATAFILE,
				storePath + defaultConfigProperties.getProperty(PROPERTY_XLDATAFILE));
		copiedProperties.setProperty(PROPERTY_WORDFORMATFILE,
				storePath + defaultConfigProperties.getProperty(PROPERTY_WORDFORMATFILE));
		copiedProperties.setProperty(PROPERTY_RESOURCEXML,
				storePath + defaultConfigProperties.getProperty(PROPERTY_RESOURCEXML));
		
		GeneralUtils.storeProperties(storePath + CONFIG_PROPERTIES, copiedProperties);
		
		return copiedProperties;
	}

	private String createStoreAtHome() {
		System.out.println("User home detected as '" + System.getProperty("user.home") + "'");
		String userHomePath = System.getProperty("user.home");
		File dataSoreDir = new File(userHomePath, NGS_APP_DATA_DIR);
		if (!dataSoreDir.exists()) {
			boolean created = dataSoreDir.mkdirs();
			if(created) {
	            System.out.println("Direcotry '" + dataSoreDir + "' created!");
	        } else {
	        	System.out.println("Failed to create direcotry '" + dataSoreDir + "'!");
	        }
		}
		
		return dataSoreDir.getAbsolutePath() + "/";
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
