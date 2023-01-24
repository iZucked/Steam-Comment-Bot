/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.debug;

/**
 * Constants for the cloud optimiser logging.
 * 
 * Remember to update the .options file in the project root for Eclipse run/debug dialog integration
 * 
 * @author Simon Goodall
 *
 */
public class CloudOptiDebugContants {

	private CloudOptiDebugContants() {

	}

	public static final String ID_BASE = "com.mmxlabs.lngdataserver.integration.ui.scenarios/debug/cloud/";

	public static final int NUM_TEMP_FILE_BYTES_TO_PRINT = 512;

	public static final String DEBUG_DOWNLOAD = ID_BASE + "resultdownload";
	public static final String DEBUG_IMPORT = ID_BASE + "resultimport";
	public static final String DEBUG_POLL = ID_BASE + "polling";
}
