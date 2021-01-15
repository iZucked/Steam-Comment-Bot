/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LicenseCheckerTests {

	@Test
	public void testLoadingFromURIWithSpaces() throws Exception {

		Path tempDir = Files.createTempDirectory("lingo test spaces");
		try {
			String location = "file:///" + tempDir.toString().replaceAll("\\\\", "/");
			System.out.println(location);
			File f = LicenseChecker.getCACertsFileFromEclipseHomeURL(location);

			Assertions.assertNotNull(f);
		} finally {
			Files.delete(tempDir);
		}

		// LicenseChecker.
	}
}
