/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions.ScenarioFileFormat;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.common.HeadlessRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserRunner;

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
	protected void runAndWriteResults(int run, HeadlessApplicationOptions hOptions, File scenarioFile, File outputFile, int threads) throws Exception {
		HeadlessOptimiserJSON json = (new HeadlessOptimiserJSONTransformer()).createJSONResultObject(getDefaultMachineInfo(), scenarioFile, threads);
		writeMetaFields(json, scenarioFile, hOptions, threads);

		HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();

		ScenarioFileFormat type = hOptions.getExpectedScenarioFormat();

		switch (type) {
		case LINGO_FILE: {
			runner.run(scenarioFile, hOptions, new NullProgressMonitor(), null, json);
			break;
		}
		case CSV_FOLDER: {
			runner.runFromCsvDirectory(scenarioFile, hOptions, new NullProgressMonitor(), null, json);
			break;
		}
		case CSV_ZIP: {
			runner.runFromCsvZipFile(scenarioFile, hOptions, new NullProgressMonitor(), null, json);
			break;
		}
		default: {
			throw new UnhandledScenarioTypeException(String.format("No method for handling scenario file %s", scenarioFile.getName()));
		}
		}

		HeadlessRunnerUtils.renameInvalidBsonFields(json.getMetrics().getStages());

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			CustomTypeResolverBuilder resolver = new CustomTypeResolverBuilder();
			resolver.init(JsonTypeInfo.Id.CLASS, null);
			resolver.inclusion(JsonTypeInfo.As.PROPERTY);
			resolver.typeProperty("@class");
			mapper.setDefaultTyping(resolver);

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, json);
		} catch (Exception e) {
			System.err.println("Error writing to file:");
			e.printStackTrace();
		}

	}

	@Override
	protected String getAlgorithmName() {
		// TODO: use this in the created log
		return "optimisation";
	}

}
