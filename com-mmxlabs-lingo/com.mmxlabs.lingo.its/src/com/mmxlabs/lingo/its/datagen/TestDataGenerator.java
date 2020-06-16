/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datagen;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.scenario.utils.ExportCSVBundleUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class TestDataGenerator {

	@Test
	public void generateTestDataCSVBundle() throws Exception {

		String workspace = System.getProperty("WORKSPACE");
		
		System.out.println("WORKSPACE IS " + workspace);
		if (workspace != null && !workspace.isEmpty()) {
			final File tempFile = Files.createFile(Path.of(workspace, "testdatabundle.zip")).toFile();
			System.out.println("BUMNDLE FILE  IS " + tempFile);
			final ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
			sb.loadDefaultData();
			IScenarioDataProvider scenarioDataProvider = sb.getScenarioDataProvider();

			ExportCSVBundleUtil.exportScenarioToZip(scenarioDataProvider, tempFile);
		}
	}
}
