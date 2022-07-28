/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions.ScenarioFileFormat;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.CSVImporter;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimisationJobRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptimiserApplication extends HeadlessGenericApplication {

	/**
	 * Runs the optimiser and writes the output log results.
	 * 
	 * The headless optimiser shares a lot of logic with the headless optioniser,
	 * but the code paths differ significantly, particularly the code that actually
	 * writes data to the output object.
	 * 
	 * Hence, this method does some things that ought to be handled in
	 * HeadlessGenericApplication. Future implementations should probably refactor
	 * this method and clean up the separation of concerns.
	 */
	protected void runAndWriteResults(final int run, final HeadlessApplicationOptions hOptions, final File scenarioFile, final File outputFile, final int threads) throws Exception {
		final Meta meta = writeMetaFields(scenarioFile, hOptions);
		meta.setScenario(scenarioFile.getName());

		final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
		jobRunner.withLogging(meta);

		File bundledParams = new File(HeadlessApplicationOptions.fileNameWithoutExt(scenarioFile.getAbsolutePath()) + ".userSettings.json");
		if (bundledParams.exists()) {
			jobRunner.withParams(bundledParams);
		} else {

			File file = new File(hOptions.algorithmConfigFile);
			if (file.exists()) {
				jobRunner.withParams(file);
			} else {
				throw new IllegalStateException("No optimisation parameters found");
			}
		}
		// Consumer taking the SDP. We need to run within the action to avoid the SDP
		// being closed when the scope closes
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
		return "optimisation";
	}

}
