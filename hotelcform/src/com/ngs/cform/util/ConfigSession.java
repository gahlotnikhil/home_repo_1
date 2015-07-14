package com.ngs.cform.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.ngs.cform.ProgressBar;
import com.ngs.cform.field.FormFieldGenerator;
import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.manager.WordManager;
import com.ngs.cform.resource.ResourceConfig;

public class ConfigSession {
	
	public static String NGS_APP_DATA_DIR = "ngs-app-data";
	
	private static ConfigSession session;
	
	private ExcelDataManager dataManager;
    private WordManager wordManager;
    private FormFieldGenerator fieldGenerator;
    private Properties configProperties;
    private ResourceConfig resourceConfig;
    private Properties properties;
    private ProgressBar progressBar;
	
	private ConfigSession(ProgressBar progressBar) {
		this.progressBar = progressBar;
		loadConfigFiles();
	}
	
	public static ConfigSession getConfigSession() {
		if (session == null) {
			session = loadSession(null);
		}
		return session;
	}
	
	public static ConfigSession loadSession(ProgressBar progressBar) {
		if (session == null) {
			session = new ConfigSession(progressBar);
		}
		
		return session;
	}

	private void loadConfigFiles() {
		try {
			// use this file from outside the application eg: /home/ngs-app-data/config.properties
			String storePath = createStoreAtHome();
			progressBar.addProgress(5);
			// Store all the files in the same location as default
			// User defined values should override values in ~home/config.properties
			// Defaults will always be there in the config.properties as part of the application.
			configProperties = copyConfigFilesToStore(storePath);
			progressBar.addProgress(10);
			
			resourceConfig = new ResourceConfig(configProperties.get(PROPERTY_RESOURCEXML) != null ? String.valueOf(configProperties.get(PROPERTY_RESOURCEXML)) : null);
			
			properties = GeneralUtils.loadProperties(storePath + RESOURCE_PROPERTIES, true);
			progressBar.addProgress(10);
			
			File file = new File(String.valueOf(configProperties.get(PROPERTY_XLDATAFILE)));
			progressBar.addProgress(3);
			dataManager = new ExcelDataManager(file, resourceConfig);
			progressBar.addProgress(15);
			wordManager = new WordManager(new File(String.valueOf(configProperties.get(PROPERTY_WORDFORMATFILE))), properties);
			progressBar.addProgress(10);
			
			fieldGenerator = new FormFieldGenerator(resourceConfig, properties);
			progressBar.addProgress(10);
			
			// Initialise log
			new LogConfig(storePath + "app.log");
			
			progressBar.addProgress(5);
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
