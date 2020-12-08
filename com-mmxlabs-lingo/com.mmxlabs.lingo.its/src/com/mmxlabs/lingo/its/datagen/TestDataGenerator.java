/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datagen;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.scenario.utils.ExportCSVBundleUtil;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@RequireFeature(KnownFeatures.FEATURE_PAPER_DEALS) // Needed to export data correctly
public class TestDataGenerator {

	@Test
	public void generateTestDataCSVBundle() throws Exception {

		// Prop osgi.instance.area C-> file:/home/jenkins/workspace/master/LiNGO-ITS-master/com-mmxlabs-lingo/com.mmxlabs.lingo.its/target/work/data/

		String workspace = System.getProperty("osgi.instance.area");

		if (workspace != null && !workspace.isEmpty()) {

			String prefix;
			if (System.getProperty("os.name").startsWith("Windows")) {
				prefix = "file:/";
			} else {
				prefix = "file:";
			}

			// Note: Currently breaks on windows. Needs a different file:/// prefix
			final File tempFile = Files.createFile(Path.of(workspace.replaceAll(prefix, ""), "testdatabundle.zip")).toFile();
			final ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
			sb.loadDefaultData();
			sb.createDummyPricingData();

			IScenarioDataProvider scenarioDataProvider = sb.getScenarioDataProvider();

			ExportCSVBundleUtil.exportScenarioToZip(scenarioDataProvider, tempFile);
		}
	}
}
