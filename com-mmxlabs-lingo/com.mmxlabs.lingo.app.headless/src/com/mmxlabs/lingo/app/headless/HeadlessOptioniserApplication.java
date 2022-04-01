/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions.ScenarioFileFormat;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.CSVImporter;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserJobRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptioniserApplication extends HeadlessGenericApplication {

	protected void runAndWriteResults(int run, HeadlessApplicationOptions hOptions, File scenarioFile, File outputFile, int threads) throws Exception {
		
		final Meta meta = writeMetaFields(scenarioFile, hOptions);
		meta.setScenario(scenarioFile.getName());
		
		final OptioniserJobRunner jobRunner = new OptioniserJobRunner();
		jobRunner.withLogging(meta);
		jobRunner.withParams(new File(hOptions.algorithmConfigFile));
		
		// Consumer taking the SDP. We need to run within the action to avoid the SDP being closed when the scope closes
		final CheckedBiConsumer<ScenarioModelRecord, IScenarioDataProvider, Exception> runnerAction = (modelRecord, scenarioDataProvider) -> {
			jobRunner.withScenario(scenarioDataProvider);
			jobRunner.run(0, new NullProgressMonitor());
		};
		
		final ScenarioFileFormat type = hOptions.getExpectedScenarioFormat();
		switch (type) {
		case LINGO_FILE -> ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), runnerAction);
		case CSV_FOLDER -> CSVImporter.runFromCSVDirectory(scenarioFile, runnerAction);
		case CSV_ZIP -> CSVImporter.runFromCSVZipFile(scenarioFile, runnerAction);
		default -> throw new UnhandledScenarioTypeException(String.format("No method for handling scenario file %s", scenarioFile.getName()));
		}

		jobRunner.saveLogs(outputFile);
	}

	@Override
	protected String getAlgorithmName() {
		// TODO: use this in the created log
		return "optioniser";
	}

}
