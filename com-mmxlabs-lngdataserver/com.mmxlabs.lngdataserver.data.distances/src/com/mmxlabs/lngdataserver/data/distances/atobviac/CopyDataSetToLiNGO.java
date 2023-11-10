/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

import com.google.common.io.Files;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.DistancesLinesToScenarioCopier;

public class CopyDataSetToLiNGO {
	public static void main(final String[] args) throws Exception {

		final String sourceData = "2023f"; // Original data set

		copy(sourceData, "ports.json");
		copy(sourceData, "distances.json");
		copy(sourceData, "distances-version.json");

	}

	public static void copy(final String sourceData, String file) throws Exception {

		final URL baseURL = CopyDataSetToLiNGO.class.getResource("/" + sourceData + "/" + file);
		final URL baseFileURL = new URL(FileLocator.toFileURL(new URL(baseURL.toString())).toString().replace(" ", "%20"));
		final File baseFile = new File(baseFileURL.toURI());

		final URL targetURL = DistancesLinesToScenarioCopier.class.getResource("/distances-version.json");
		final URL targetFileURL = new URL(FileLocator.toFileURL(new URL(targetURL.toString())).toString().replace(" ", "%20").replace("distances-version.json", file));
		final File targetFile = new File(targetFileURL.toURI());

		Files.copy(baseFile, targetFile);
	}
}
