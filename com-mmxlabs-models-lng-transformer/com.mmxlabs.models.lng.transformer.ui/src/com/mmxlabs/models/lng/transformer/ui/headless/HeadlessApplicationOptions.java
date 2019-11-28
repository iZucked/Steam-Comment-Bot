/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;
import java.util.HashMap;

public class HeadlessApplicationOptions {
	/**
	 * The name of the scenario being run.
	 */
	public String scenarioFileName;
	
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
}