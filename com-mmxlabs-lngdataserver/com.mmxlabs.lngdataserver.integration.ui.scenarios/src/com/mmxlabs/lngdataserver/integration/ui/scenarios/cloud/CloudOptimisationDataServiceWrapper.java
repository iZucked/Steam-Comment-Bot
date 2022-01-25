/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;

import com.mmxlabs.hub.common.http.IProgressListener;

public class CloudOptimisationDataServiceWrapper {
	public static String uploadData(final File dataFile, //
			final String checksum, //
			final String scenarioName, //
			final IProgressListener progressListener) throws Exception {
		return CloudOptimisationDataService.INSTANCE.uploadData(dataFile, checksum, scenarioName, progressListener);
	}

	public static void updateRecords(CloudOptimisationDataResultRecord record) {
		CloudOptimisationDataService.INSTANCE.addRecord(record);
	}
}
