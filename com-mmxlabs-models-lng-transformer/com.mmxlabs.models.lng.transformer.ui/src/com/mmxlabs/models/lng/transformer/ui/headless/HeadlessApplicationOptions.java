/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

public class HeadlessApplicationOptions {
	/**
	 * The name of the scenario being run.
	 */
	public String scenarioFileName;
	
	/**
	 * The use-case handle associated with the data.
	 */
	public String useCase;
	
	/**
	 * The name of the file containing config information for the algorithm being run on the scenario.
	 */
	public String algorithmConfigFile; 

	/**
	 * The name of the scenario file to be output by the algorithm.
	 */
	public String outputScenarioFileName;
	
	/**
	 * The name of the folder to log algorithm metrics in.
	 */
	public String outputLoggingFolder;
	
	/**
	 * Any additional custom information
	 */
	public HashMap<String, String> customInfo = new HashMap<>();  
	
	/**
	 * The number of replications to use: the algorithm will be re-run this many times with the same parameters.
	 */
	public int numRuns = 1;

	/**
	 * How often to log results
	 */
	public int loggingInterval = 10000;
	
	/**
	 * Logging parameters for these runs
	 */
	public LSOLogger.LoggingParameters loggingParameters;
	
	/** 
	 * A {@link UserSettings} object to create the algorithm's config data from, if no config file is provided.
	 */
	private UserSettingsImpl userSettings;
	
	private UserSettingsImpl bundledUserSettings;
	private boolean attemptedToReadBundledSettings = false;
	
	public boolean isLoggingExportRequired() {
		return outputLoggingFolder != null;
	}	
	
	public boolean isScenarioOutputRequired() {
		return outputScenarioFileName != null && !outputScenarioFileName.isEmpty();
	}
	/**
	 * An enumeration indicating the file format that a scenario is stored in.
	 * <ul>
	 *   <li>LINGO_FILE: a ".lingo" file</li>
	 *   <li>CSV_FOLDER: a directory containing CSV files</li>
	 *   <li>CSV_ZIP: a zip archive containing CSV files</li>
	 *   <li>NONE: no file provided</li>
	 *   <li>UNRECOGNISED: any other file format</li>
	 * </ul>
	 * 
	 * @author simonmcgregor
	 *
	 */
	public enum ScenarioFileFormat { LINGO_FILE, CSV_FOLDER, CSV_ZIP, UNRECOGNISED, NONE }
	
	/**
	 * Returns the expected file format for the scenario, based on whether the supplied filename
	 * refers to a file or a directory, and its file extension if it is a file.
	 * @return ScenarioFileFormat.NONE (if the file does not exist), ScenarioFileFormat.CSV_FOLDER (for a directory), ScenarioFileFormat.LINGO_FILE (for a file with extension ".lingo"), ScenarioFileFormat.CSV_ZIP (for a file with extension ".zip"), ScenarioFileFormat.UNRECOGNISED (otherwise) 
	 */
	public ScenarioFileFormat getExpectedScenarioFormat() {
		File file = new File(scenarioFileName);
		
		if (!file.exists()) {
			return ScenarioFileFormat.NONE;
		}
		else if (file.isDirectory()) {
			return ScenarioFileFormat.CSV_FOLDER;
		}
		
		String lowerCaseFileName = scenarioFileName.toLowerCase();
		
		if (lowerCaseFileName.endsWith(".lingo")) {
			return ScenarioFileFormat.LINGO_FILE;
		}
		else if (lowerCaseFileName.endsWith(".zip")) {
			return ScenarioFileFormat.CSV_ZIP;
		}
		
		// TODO: allow a URL to be provided. 
		
		return ScenarioFileFormat.UNRECOGNISED;
	}
	
	public void validate() throws IllegalArgumentException {
		if ( outputLoggingFolder != null && loggingParameters == null) {
			throw new IllegalArgumentException("Output logging folder specified but no logging parameters provided.");
		}
		
		if ( outputLoggingFolder == null && loggingParameters != null) {
			throw new IllegalArgumentException("Logging parameters specified but no output logging folder provided.");
		}
		
		UserSettings effectiveUserSettings = getUserSettingsContent();
		
		if ( (effectiveUserSettings == null) && (algorithmConfigFile == null) ) {			
			throw new IllegalArgumentException("If algorithmConfigFile is not specified, user settings must be provided via userSettings field or bundled with scenario file.");
		}
		
		if ( (userSettings != null) && (algorithmConfigFile != null) ) {
			throw new IllegalArgumentException("Cannot specify both userSettings and algorithmConfigFile (userSettings is used to generate a config if set).");			
		}
	}

	public static String fileNameWithoutExt(String fileName) {
		if (fileName != null) {
			int i = fileName.lastIndexOf(".");
			if (i >= 0) {
				return fileName.substring(0, i);
			}
			else {
				return fileName;
			}
		}
		else {
			return null;
		}
	}	
	
	private UserSettingsImpl getBundledUserSettings() {
		if (attemptedToReadBundledSettings) {
			return bundledUserSettings;
		}
		
		attemptedToReadBundledSettings = true;
		
		System.out.println("Attempting to read bundled user settings based on scenario path.");
		
		if (scenarioFileName == null) {
			return null;
		}
		String userSettingsName = fileNameWithoutExt(scenarioFileName) + ".userSettings.json";
		
		File file = new File(userSettingsName);

		if (file.exists() == false) {
			return null;
		}
		else {

			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.enable(Feature.ALLOW_COMMENTS);
	
			try {
				bundledUserSettings = mapper.readValue(file, UserSettingsImpl.class);
				System.out.println("Setting user settings from bundled file.");
				return bundledUserSettings;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}			
		}
		
		
	}
	
	/**
	 * Provides the user settings content from the userSettings field or by loading it from a "bundled" user settings file.
	 * @return
	 */
	public UserSettingsImpl getUserSettingsContent() {
		if (userSettings != null) {
			return userSettings;
		}
		
		if (algorithmConfigFile != null) {
			System.out.println("Ignoring bundled user settings (explicit optimisation plan provided).");
			return null;
		}
		
		return getBundledUserSettings();		
	}
}