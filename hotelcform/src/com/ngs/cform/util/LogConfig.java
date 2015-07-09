package com.ngs.cform.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogConfig {

	public LogConfig(String log4jConfigFile) {
		
		PatternLayout layout = new PatternLayout();
		String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
		layout.setConversionPattern(conversionPattern);
	
		// creates console appender
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(layout);
		consoleAppender.activateOptions();
	
		// creates file appender
		FileAppender fileAppender = new FileAppender();
		fileAppender.setFile(log4jConfigFile);
		fileAppender.setLayout(layout);
		fileAppender.activateOptions();
		fileAppender.setAppend(false);
	
		// configures the root logger
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(consoleAppender);
		rootLogger.addAppender(fileAppender);
	
	}
}
